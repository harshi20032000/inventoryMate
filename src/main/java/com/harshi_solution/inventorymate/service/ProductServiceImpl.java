package com.harshi_solution.inventorymate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshi_solution.inventorymate.entities.Product;
import com.harshi_solution.inventorymate.repo.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;


	@Override
	public List<Product> getProductsList() {
		return productRepository.findAll();
	}


	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}


	@Override
	public Product getProductById(Long productId) {
		return productRepository.findById(productId).get();
	}

}
