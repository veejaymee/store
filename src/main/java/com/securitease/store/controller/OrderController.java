package com.securitease.store.controller;

import com.securitease.store.api.OrderApi;
import com.securitease.store.model.CreateOrderRequest;
import com.securitease.store.model.GetAllOrders200Response;
import com.securitease.store.model.GetAllOrders200ResponsePageable;
import com.securitease.store.model.OrderDTO;
import com.securitease.store.service.OrderService;

import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    @SneakyThrows
    public ResponseEntity<GetAllOrders200Response> getAllOrders(Integer page, Integer size, String sort) {
        Sort sortOrder = Sort.by(Sort.Order.asc("id"));
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
                sortOrder = Sort.by(new Sort.Order(direction, sortParams[0]));
            }
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<OrderDTO> pageOrders = orderService.getAllOrders(pageable);
        GetAllOrders200Response response = new GetAllOrders200Response();
        response.setContent(pageOrders.getContent());
        response.setTotalElements((int) pageOrders.getTotalElements());
        response.setTotalPages(pageOrders.getTotalPages());
        response.setSize(pageOrders.getSize());
        response.setNumberOfElements(pageOrders.getNumberOfElements());
        response.setNumber(pageOrders.getNumber());

        GetAllOrders200ResponsePageable pageableDetails = new GetAllOrders200ResponsePageable();
        pageableDetails.setPageNumber(pageOrders.getNumber());
        pageableDetails.setPageSize(pageOrders.getSize());

        // Set pageable details into the response
        response.setPageable(pageableDetails);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") String orderId) {
        return orderService
                .getOrderById(Long.valueOf(orderId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SneakyThrows
    @Override
    public ResponseEntity<OrderDTO> createOrder(CreateOrderRequest createOrderRequest) {
        OrderDTO orderDTO = orderService.createOrder(createOrderRequest);
        return ResponseEntity.status(201).body(orderDTO);
    }
}
