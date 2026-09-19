package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Category;
import javafx.collections.ObservableList;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz DAO layer for {@link Category}
 */
public interface CategoryDao {

    ObservableList<Category> getCategoryList();

    ObservableList<String> getCategoryName();

    void add(Category category);

    int getId(String name);

    String getDph(String name);

    void deleteCategory(String name);

    void saveOrUpdate(String name, String tax);

    String getTax(int id);

    String getName(String id);
}
