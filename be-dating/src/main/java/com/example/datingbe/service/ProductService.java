package com.example.datingbe.service;

import com.example.datingbe.entity.Product;
import com.example.datingbe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product Save (Product product){
      // logic
      productRepository.save(product);
      return product;
    }
    public List<Product> getAllProducts (){
        return productRepository.findAll();
    }

    public Page<Product> getAllProductPages (Pageable pageable){
        return productRepository.findAll(pageable);
    }


}
