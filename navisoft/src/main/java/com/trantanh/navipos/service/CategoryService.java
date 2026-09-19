package com.trantanh.navipos.service;

import com.trantanh.navipos.model.Category;
import javafx.collections.ObservableList;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public interface CategoryService {

    String getTax(int id);
    ObservableList<Category> getCategoryList();
    ObservableList<String> getCategoryName();
    void add(Category category);
    int getId(String name);
    String getDph(String name);
    void deleteCategory(String name);
    void saveOrUpdate(String name, String tax);

    String getName(String id);
}
