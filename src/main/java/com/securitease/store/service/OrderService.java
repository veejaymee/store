package com.securitease.store.service;

import com.securitease.store.entity.Customer;
import com.securitease.store.entity.Order;
import com.securitease.store.entity.Product;
import com.securitease.store.mapper.OrderMapper;
import com.securitease.store.model.CreateOrderRequest;
import com.securitease.store.model.OrderDTO;
import com.securitease.store.model.OrderProductDTO;
import com.securitease.store.repository.CustomerRepository;
import com.securitease.store.repository.OrderRepository;

import com.securitease.store.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public Optional<OrderDTO> getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            orderDTO.setCustomerId(order.getCustomer().getId().toString());
            orderDTO.setProducts(order.getOrderProducts().stream()
                    .map(op -> new OrderProductDTO(op.getProduct().getId().toString(), op.getQuantity()))
                    .collect(Collectors.toList()));
            return orderDTO;
        });
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<Order> pageOrders = orderRepository.findAll(pageable);

        return pageOrders.map(order -> {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            orderDTO.setCustomerId(order.getCustomer().getId().toString());
            orderDTO.setProducts(order.getOrderProducts().stream()
                    .map(op -> new OrderProductDTO(op.getProduct().getId().toString(), op.getQuantity()))
                    .collect(Collectors.toList()));
            return orderDTO;
        });
    }

    public OrderDTO createOrder(CreateOrderRequest createOrderRequest) {
        Customer customer = customerRepository.findById(Long.valueOf(createOrderRequest.getCustomerId()))
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setDescription(createOrderRequest.getDescription());
        order.setCustomer(customer);


        List<Product> products = createOrderRequest.getProductIds().stream()
                .map(productRequest -> {
                    Product product = productRepository.findById(Long.valueOf(productRequest.getProductId()))
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product;
                })
                .collect(Collectors.toList());

        order.setProducts(products);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.orderToOrderDTO(savedOrder);
    }

}
