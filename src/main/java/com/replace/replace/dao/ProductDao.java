package com.replace.replace.dao;

import com.replace.replace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository< Product, Integer > {

    Product findById( int id );

    List< Product > findByPriceGreaterThan( double price );
}
