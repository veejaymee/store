package com.securitease.store.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "\"customer\"")
@SequenceGenerator(name = "customer_seq", sequenceName = "customer_id_seq", allocationSize = 1)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
