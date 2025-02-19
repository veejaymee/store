package com.securitease.store.mapper;

import com.securitease.store.entity.Order;
import com.securitease.store.entity.Product;
import com.securitease.store.model.OrderDTO;

import com.securitease.store.model.OrderProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "products", target = "products")
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(target = "quantity", ignore = true)
    OrderProductDTO productToOrderProductDTO(Product product);

}
