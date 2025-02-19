package com.securitease.store.repository;

import com.securitease.store.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository  extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByProductId(Long id);
}

