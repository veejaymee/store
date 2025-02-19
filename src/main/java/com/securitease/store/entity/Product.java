package com.securitease.store.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@SequenceGenerator(name = "product_seq", sequenceName = "product_id_seq", allocationSize = 1)
public class Product {

    public Product() {}

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderProduct> orderProducts;

}
