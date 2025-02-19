package com.securitease.store.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "order_product")
@Data
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Embeddable
    @Data
    public static class OrderProductKey implements Serializable {
        private Long orderId;
        private Long productId;
    }
}
