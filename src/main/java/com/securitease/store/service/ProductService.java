package com.securitease.store.service;

import com.securitease.store.entity.Product;
import com.securitease.store.mapper.ProductMapper;
import com.securitease.store.model.CreateProductRequest;
import com.securitease.store.model.ProductDTO;
import com.securitease.store.repository.OrderProductRepository;
import com.securitease.store.repository.OrderRepository;
import com.securitease.store.repository.ProductRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private  final OrderProductRepository orderProductRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, OrderProductRepository orderProductRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.productMapper = productMapper;
    }

    @SneakyThrows
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToProductDTO(product);
    }

    @SneakyThrows
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductDTO).collect(Collectors.toList());
    }

    @Transactional
    @SneakyThrows
    public ProductDTO createProduct(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setPrice(BigDecimal.valueOf(createProductRequest.getPrice()));
        return mapToProductDTO(productRepository.save(product));

    }

    private ProductDTO mapToProductDTO(Product product) {
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        List<String> orderIds = orderProductRepository.findByProductId(product.getId())
                .stream()
                .map(orderProduct -> orderProduct.getOrder().getId().toString())
                .collect(Collectors.toList());

        productDTO.setOrderIds(orderIds);
        return productDTO;
    }
}
