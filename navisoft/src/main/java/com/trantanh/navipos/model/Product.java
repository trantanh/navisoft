package com.trantanh.navipos.model;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class Product {

    private int id;
    private String name;
    private String price;
    private String barcode;
    private String quantity;
    private String category;
    private String dph;
    private String priceWithoutTax;
    private String unit;

    public Product(String name, String price, String barcode, String quantity) {
        this.name = name;
        this.price = price;
        this.barcode = barcode;
        this.quantity = quantity;
        this.dph = "";
        this.category = "";
        this.priceWithoutTax = "";
    }

    public Product(int id, String name, String price, String barcode, String dph, String category, String quantity, String priceWithoutTax, String unit) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barcode = barcode;
        this.quantity = quantity;
        this.dph = dph;
        this.category = category;
        this.priceWithoutTax = priceWithoutTax;
        this.unit = unit;
    }

    private Product(builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.barcode = builder.barcode;
        this.quantity = builder.quantity;
        this.dph = builder.dph;
        this.category = builder.category;
        this.priceWithoutTax = builder.priceWithoutTax;
        this.unit = builder.unit;
    }

    public static class builder {
        private int id;
        private String name;
        private String price;
        private String barcode;
        private String quantity;
        private String category;
        private String dph;
        private String priceWithoutTax;
        private String unit;

        public builder() {

        }

        public builder setId(int id) {
            this.id = id;
            return this;
        }

        public builder setName(String name) {
            this.name = name;
            return this;
        }

        public builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public builder setBarcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public builder setQuantity(String quantity) {
            this.quantity = quantity;
            return this;
        }

        public builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public builder setDph(String dph) {
            this.dph = dph;
            return this;
        }

        public builder setPriceWithoutTax(String priceWithoutTax) {
            this.priceWithoutTax = priceWithoutTax;
            return this;
        }

        public builder setUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDph() {
        return dph;
    }

    public void setDph(String dph) {
        this.dph = dph;
    }

    public String getPriceWithoutTax() {
        return priceWithoutTax;
    }

    public void setPriceWithoutTax(String priceWithoutTax) {
        this.priceWithoutTax = priceWithoutTax;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (barcode != null ? !barcode.equals(product.barcode) : product.barcode != null) return false;
        if (quantity != null ? !quantity.equals(product.quantity) : product.quantity != null) return false;
        if (category != null ? !category.equals(product.category) : product.category != null) return false;
        if (dph != null ? !dph.equals(product.dph) : product.dph != null) return false;
        return priceWithoutTax != null ? priceWithoutTax.equals(product.priceWithoutTax) : product.priceWithoutTax == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (barcode != null ? barcode.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (dph != null ? dph.hashCode() : 0);
        result = 31 * result + (priceWithoutTax != null ? priceWithoutTax.hashCode() : 0);
        return result;
    }
}
