package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.CategoryDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class CategoryDaoImpl implements CategoryDao {

    private final static String TAX21 = "21";
    private DatabaseManager<Category> categoryDatabaseManager;

    public CategoryDaoImpl() {
        categoryDatabaseManager = new DatabaseManager<>();
    }

    @Override
    public ObservableList<Category> getCategoryList() {
        categoryDatabaseManager.setup();
        List<Category> categoryList = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        ObservableList<Category> data = FXCollections.observableArrayList();
        data.addAll(categoryList);
        return data;
    }

    @Override
    public ObservableList<String> getCategoryName() {
        categoryDatabaseManager.setup();
        List<Category> categoryList = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        ObservableList<String> data = FXCollections.observableArrayList();
        for (Category category : categoryList) {
            data.add(category.getName());
        }
        return data;
    }

    @Override
    public void add(Category category) {
        categoryDatabaseManager.setup();
        categoryDatabaseManager.saveOrUpdate(category);
        categoryDatabaseManager.exit();
    }

    @Override
    public int getId(String name) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category.getId();
            }
        }
        return 0;
    }

    @Override
    public String getDph(String name) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category.getDph();
            }
        }
        return TAX21;
    }

    @Override
    public void deleteCategory(String name) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                categoryDatabaseManager.delete(category);
            }
        }
        categoryDatabaseManager.exit();

    }

    @Override
    public void saveOrUpdate(String name, String tax) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                category.setDph(tax);
                categoryDatabaseManager.saveOrUpdate(category);
            }
        }

        categoryDatabaseManager.exit();
    }

    @Override
    public String getName(String id) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        for (Category category : categories) {
            if (category.getId() == Integer.parseInt(id)) {
                return category.getName();
            }
        }
        return "NO NAME";
    }

    @Override
    public String getTax(int id) {
        categoryDatabaseManager.setup();
        List<Category> categories = categoryDatabaseManager.findAll(Category.class);
        categoryDatabaseManager.exit();
        for (Category category : categories) {
            if (category.getId() == id) {
                return category.getDph();
            }
        }
        return TAX21;
    }
}
