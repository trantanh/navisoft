package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.dao.ProductDao;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.dao.impl.ProductDaoImpl;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Tax;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.utils.DateUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class ProductServiceImpl implements ProductService {

    private final String PRODUCT_BREAD = "100001";
    private final String PRODUCT_GROCERY = "100002";
    private final String PRODUCT_ALCOHOL = "100003";
    private final String PRODUCT_CIGARETTES = "100004";
    private final String PRODUCT_FRUIT = "100005";
    private final String PRODUCT_VEGETABLES = "100006";
    private final String PRODUCT_DRUGSTORE = "100007";
    private final String PRODUCT_BACKUP_BOTTLE = "100008";
    private ProductDao productDao = new ProductDaoImpl();

    private final static ConcurrentHashMap<String, Product> productCache = new ConcurrentHashMap<>();
    private final List<String> productCodes = new ArrayList<>(Arrays.asList(PRODUCT_BREAD, PRODUCT_GROCERY, PRODUCT_ALCOHOL, PRODUCT_CIGARETTES, PRODUCT_VEGETABLES, PRODUCT_DRUGSTORE, PRODUCT_FRUIT, PRODUCT_BACKUP_BOTTLE));

    private static ProductServiceImpl instance = null;
    private CategoryDaoImpl categoryData = new CategoryDaoImpl();

    private ProductServiceImpl() {
        List<Product> products = productDao.findAll();
        products.forEach(product -> {
            productCache.put(product.getBarcode(), product);
        });
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductServiceImpl();
        }
        return ProductServiceImpl.instance;
    }

    @Override
    public Product searchProduct(String barcode) {
        if (productCache.containsKey(barcode)) {
            return productCache.get(barcode);
        } else {
            return productDao.findByBarcode(barcode);
        }
    }

    @Override
    public int getCategoryId(String barcode) {
        return productDao.getCategoryId(barcode);
    }

    @Override
    public void addProduct(String name, String priceText, String barcode, String quantity, String categoryName) {
        double price = Double.parseDouble(priceText);
        int categoryId = categoryData.getId(categoryName);
        if (categoryId != 0) {
            productDao.addProduct(name, DateUtils.format(price), barcode, quantity, categoryId);
        }
        updateCache();
    }

    @Override
    public boolean validatorBarcodeProduct(String barcode) {
        if (productCache.containsKey(barcode)) {
            return true;
        } else {
            Product product = productDao.findByBarcode(barcode);
            if (product != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public List<Product> favoriteProducts() {
        List<Product> favoriteProducts = new ArrayList<>();
        productCodes.forEach(productCode -> {
            Product product = searchProduct(productCode);
            if (product != null) {
                favoriteProducts.add(product);
            } else {
                initFavoriteProduct(productCode);
                product = searchProduct(productCode);
                if (product != null) {
                    favoriteProducts.add(product);
                }
            }
        });
        return favoriteProducts;
    }

    private void initFavoriteProduct(String barcode) {
        if (barcode.equals(PRODUCT_BREAD)) {
            addProduct("Pecivo", "0", PRODUCT_BREAD, "0", "Potraviny");
        }
        if (barcode.equals(PRODUCT_GROCERY)) {
            addProduct("Potraviny", "0", PRODUCT_GROCERY, "0", "Potraviny");
        }
        if (barcode.equals(PRODUCT_ALCOHOL)) {
            addProduct("Alkohol", "0", PRODUCT_ALCOHOL, "0", "Alkohol");
        }
        if (barcode.equals(PRODUCT_CIGARETTES)) {
            addProduct("Cigarety", "0", PRODUCT_CIGARETTES, "0", "Cigarety");
        }
        if (barcode.equals(PRODUCT_FRUIT)) {
            addProduct("Ovoce", "0", PRODUCT_FRUIT, "0", "Ovoce a Zelenina");
        }
        if (barcode.equals(PRODUCT_VEGETABLES)) {
            addProduct("Zelenina", "0", PRODUCT_VEGETABLES, "0", "Ovoce a Zelenina");
        }
        if (barcode.equals(PRODUCT_DRUGSTORE)) {
            addProduct("Drogerie", "0", PRODUCT_DRUGSTORE, "0", "Drogerie");
        }
        if (barcode.equals(PRODUCT_BACKUP_BOTTLE)) {
            addProduct("Vratné lahve", "0", PRODUCT_BACKUP_BOTTLE, "0", "Ostatni");
        }
    }

    @Override
    public void updateUnit(String unit, String barcode) {
        productDao.updateUnit(unit, barcode);
        updateCache();

    }

    @Override
    public void countSale(int product_id, int count) {
        productDao.countSale(product_id, count);
    }

    @Override
    public void deleteProduct(String barcode) {
        productDao.deleteProduct(barcode);
    }

    @Override
    public int getId(String barcode) {
        Product product = productCache.get(barcode);
        if (product != null) {
            return product.getId();
        } else {
            return productDao.findByBarcode(barcode).getId();
        }
    }

    @Override
    public ObservableList<Product> findAllByCategory(int id) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productDao.findAllByCategory(id));
        return products;
    }

    @Override
    public ObservableList<Product> findAllByCategory(String name) {
        ObservableList<Product> data = FXCollections.observableArrayList();
        productCache.forEach((k, v) -> {
            if (v.getCategory().equals(name)) {
                data.add(v);
            }
        });
        return data;
    }

    @Override
    public Tax getTax(String barcode) {
        return productDao.getTax(barcode);
    }

    @Override
    public ObservableList<Product> getProductList() {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        List<Product> products = productDao.findAll();
        products.forEach(p -> {
            productObservableList.add(p);
        });
        return productObservableList;
    }

    @Override
    public ObservableList<Product> getProductListSale() {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        productObservableList.addAll(productDao.getProductListSale());
        return productObservableList;
    }

    @Override
    public void addDiscount(String price, String from_date, String to_date, int id) {
        productDao.addDiscount(price, from_date, to_date, id);
    }

    @Override
    public void updateProduct(String name, String price, String quantity, String category, String barcode, String id) {
        productDao.updateProduct(name, price, quantity, category, barcode, id);
        updateCache();
    }

    @Override
    public void updateProduct(String name, String newName, String barcode) {
        productDao.updateProduct(name, newName, barcode);
        updateCache();
    }

    private void updateCache() {
        productCache.clear();
        List<Product> products = productDao.findAll();
        products.forEach(product -> {
            productCache.put(product.getBarcode(), product);
        });
    }
}
