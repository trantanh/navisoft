package com.trantanh.navipos.utils;

import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Tax;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.ProductTax;
import com.trantanh.navipos.service.impl.ProductServiceImpl;
import com.trantanh.navipos.service.impl.ProductTaxImpl;
import javafx.collections.ObservableList;

/**
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public final class TaxUtils {

    private final static String TAX12 = "12";
    private final static String TAX21 = "21";

    private TaxUtils() {

    }

    public static SalesDetail taxProduct(ObservableList<Product> data) {
        double totalPriceForTax12 = 0.00;
        double totaPriceForTax21 = 0.00;
        for (Product product : data) {
            double price = Double.valueOf(product.getPrice());
            String tax = resolveTax(product);
            if (price > 0) {
                switch (tax) {
                    case TAX12:
                        totalPriceForTax12 += price * Double.parseDouble(product.getQuantity());
                        break;
                    case TAX21:
                        totaPriceForTax21 += price * Double.parseDouble(product.getQuantity());
                        break;
                    default:
                        totaPriceForTax21 += price * Double.parseDouble(product.getQuantity());
                }
            }
        }
        ProductTax productTax12 = new ProductTaxImpl(TaxType.TAX12, totalPriceForTax12);
        ProductTax productTax21 = new ProductTaxImpl(TaxType.TAX21, totaPriceForTax21);
        String zakl_dan1 = productTax21.getValueOfTax();
        String dan1 = productTax21.getBaseTax();
        String zakl_dan2 = productTax12.getValueOfTax();
        String dan2 = productTax12.getBaseTax();
        return new SalesDetail(zakl_dan2, dan2, zakl_dan1, dan1);
    }

    private static String resolveTax(Product product) {
        if (product.getDph() != null && !product.getDph().isBlank()) {
            return product.getDph();
        }

        ProductService productService = ProductServiceImpl.getInstance();
        Tax tax = productService.getTax(product.getBarcode());
        return tax == null ? TAX21 : tax.getTax();
    }

    public static String negativeTax(String tax) {
        if (tax.equals("0.00")) {
            return tax;
        } else {
            return "-" + tax;
        }
    }
}
