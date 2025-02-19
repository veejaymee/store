package com.securitease.store.controller;

import com.securitease.store.api.ProductApi;
import com.securitease.store.mapper.ProductMapper;
import com.securitease.store.model.CreateProductRequest;
import com.securitease.store.model.ProductDTO;
import com.securitease.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    @SneakyThrows
    public ResponseEntity<List<ProductDTO>> getProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Override
    @SneakyThrows
    public ResponseEntity<ProductDTO> getProductById(Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<ProductDTO> createProduct(CreateProductRequest createProductRequest) {
        return ResponseEntity.status(201).body(productService.createProduct(createProductRequest));
    }

}
