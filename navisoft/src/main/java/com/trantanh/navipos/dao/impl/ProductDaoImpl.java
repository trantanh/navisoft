package com.trantanh.navipos.dao.impl;

import java.sql.Connection;
import com.trantanh.navipos.dao.ProductDao;
import com.trantanh.navipos.model.DatabaseConnector;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Tax;
import com.trantanh.navipos.utils.DateUtils;
import com.trantanh.navipos.utils.PriceUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class ProductDaoImpl extends DatabaseConnector implements ProductDao {

    public ProductDaoImpl() {
        super();
    }

    @Override
    public void countSale(int product_id, int count) {
        try {
            String query = "SELECT * FROM `products` WHERE id =?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, product_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                int count_sale = rs.getInt("count_sale");
                count_sale += count;
                quantity -= count;
                updateCountSale(product_id, count_sale, quantity);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateCountSale(int id, int count_sale, int quantity) {
        try {
            String query = "UPDATE `products` SET `quantity`=?,`count_sale`=? WHERE `id`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, count_sale);
            preparedStatement.setInt(3, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addProduct(String name, String price, String barcode, String quantity, int categoryId) {
        String query = "INSERT INTO products(name, price, barcode,quantity,category_id, count_sale) VALUES(?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, PriceUtils.replacePriceValue(price));
            preparedStatement.setString(3, barcode);
            preparedStatement.setString(4, quantity);
            preparedStatement.setInt(5, categoryId);
            preparedStatement.setInt(6, 0);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteProduct(String barcode) {
        String query = "DELETE FROM products WHERE barcode=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Product findByBarcode(String barcode) {
        try {
            String query = "SELECT * FROM products JOIN `categories` ON `category_id` = categories.id WHERE barcode=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String dph = rs.getString("dph");
                String quantity = rs.getString("quantity");
                String unit = rs.getString("unit");
                String category = rs.getString("categories.name");
                double priceWithoutDph = Double.valueOf(PriceUtils.replacePriceValue(price));
                double dphProduct = Double.valueOf(dph) / 100;
                double dphPrice = priceWithoutDph * dphProduct;
                double priceDph = priceWithoutDph - dphPrice;
                Product product = new Product.builder()
                        .setId(id)
                        .setName(name)
                        .setPrice(price)
                        .setBarcode(barcode)
                        .setDph(dph)
                        .setCategory(category)
                        .setQuantity(quantity)
                        .setPriceWithoutTax(DateUtils.format(priceDph))
                        .setUnit(unit).build();
                return product;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int getCategoryId(String barcode) {
        try {
            String query = "SELECT category_id FROM products WHERE barcode=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<Product> findAllByCategory(int id) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products INNER JOIN categories on products.category_id=categories.id WHERE categories.id= ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                convertToProduct(products, rs);
            }
            return products;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Tax getTax(String barcode) {
        try {
            String query = "SELECT * FROM products INNER JOIN categories on products.category_id=categories.id WHERE products.barcode= ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            try (ResultSet rs = preparedStatement.executeQuery();) {
                rs.next();
                String dph = rs.getString("dph");
                String price = rs.getString("price");
                return new Tax(dph, price);
            } catch (Exception e) {
                Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void updateProduct(String name, String newName, String barcode) {
        try {
            String query = "UPDATE products set " + name + " =? WHERE barcode =?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, barcode);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateProduct(String name, String price, String quantity, String category, String barcode, String id) {
        String query = "UPDATE `products` SET `quantity`=?, `category_id` = ?, `barcode`=?, `price`=?, `name`=? WHERE `id`=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, quantity);
            preparedStatement.setString(2, category);
            preparedStatement.setString(3, barcode);
            preparedStatement.setString(4, price);
            preparedStatement.setString(5, name);
            preparedStatement.setString(6, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateUnit(String unit, String barcode) {
        try {
            String query = "UPDATE `products` SET `unit`=? WHERE `barcode`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, unit);
            preparedStatement.setString(2, barcode);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void addDiscount(String price, String from_date, String to_date, int id) {
        String query = "UPDATE `products` SET  `price2`=?,`from_date`=?, `to_date`=? WHERE `id`=?";
        try {
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, price);
            preparedStatement.setString(2, from_date);
            preparedStatement.setString(3, to_date);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List<Product> getProductListSale() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getDatabaseConnection(); Statement statement = connection.createStatement()) {
            String query = "SELECT `name`,`from_date`,`to_date`,`price2` FROM `products` WHERE `from_date`IS NOT NULL";
            statement.execute(query);
            try (ResultSet resultSet = statement.getResultSet()) {
                String name = resultSet.getString("name");
                String price = resultSet.getString("price2");
                Timestamp from_date = resultSet.getTimestamp("from_date");
                Timestamp to_date = resultSet.getTimestamp("to_date");
                Product product = new Product(name, price, DateUtils.formatDate(from_date), DateUtils.formatDate(to_date));
                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, "Cannot load all products", e);
        }
        return products;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getDatabaseConnection(); Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM `products` JOIN `categories` ON `category_id` = categories.id";
            statement.execute(query);
            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    convertToProduct(products, resultSet);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, "Cannot load all products", e);
        }
        return products;
    }

    private void convertToProduct(List<Product> products, ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String price = resultSet.getString("price");
        String barcode = resultSet.getString("barcode");
        String dph = resultSet.getString("dph");
        String quantity = resultSet.getString("quantity");
        String unit = resultSet.getString("unit");
        String category = resultSet.getString("categories.name");
        double priceWithoutDph = Double.valueOf(PriceUtils.replacePriceValue(price));
        double dphProduct = Double.valueOf(dph) / 100;
        double dphPrice = priceWithoutDph * dphProduct;
        double priceDph = priceWithoutDph - dphPrice;
        Product product = new Product.builder()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setBarcode(barcode)
                .setDph(dph)
                .setCategory(category)
                .setQuantity(quantity)
                .setPriceWithoutTax(DateUtils.format(priceDph))
                .setUnit(unit).build();
        products.add(product);
    }

    @Override
    public Product findById(int id, String quantity, String price) {
        String query = "SELECT * FROM `products` WHERE `id`=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String barcode = rs.getString("barcode");
                Product product = new Product(name, price, barcode, quantity);
                return product;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
