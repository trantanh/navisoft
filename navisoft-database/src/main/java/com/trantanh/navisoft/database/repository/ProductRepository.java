package com.trantanh.navisoft.database.repository;

import com.trantanh.navisoft.database.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ivan Tran, tran.tuan.anh@starkysclub.com
 * 21.09.2018
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
}
