package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.dao.CategoryDao;
import com.trantanh.navipos.dao.impl.CategoryDaoImpl;
import com.trantanh.navipos.model.Category;
import com.trantanh.navipos.service.CategoryService;
import javafx.collections.ObservableList;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class CategoryServiceImpl implements CategoryService {
    private static CategoryServiceImpl instance = null;
    private CategoryDao categoryDao =  new CategoryDaoImpl();;

    public static CategoryServiceImpl getInstance() {
        if (instance == null) {
            CategoryServiceImpl.instance = new CategoryServiceImpl();
        }
        return CategoryServiceImpl.instance;
    }

    @Override
    public String getTax(int id) {
        return categoryDao.getTax(id);
    }

    @Override
    public ObservableList<Category> getCategoryList() {
        return categoryDao.getCategoryList();
    }

    @Override
    public ObservableList<String> getCategoryName() {
        return categoryDao.getCategoryName();
    }

    @Override
    public void add(Category category) {
        categoryDao.add(category);
    }

    @Override
    public int getId(String name) {
        return categoryDao.getId(name);
    }

    @Override
    public String getDph(String name) {
        return categoryDao.getDph(name);
    }

    @Override
    public void deleteCategory(String name) {
        categoryDao.deleteCategory(name);
    }

    @Override
    public void saveOrUpdate(String name, String tax) {
        categoryDao.saveOrUpdate(name, tax);
    }

    @Override
    public String getName(String id) {
        return categoryDao.getName(id);
    }

}
