package com.securitease.store.controller;

import com.securitease.store.api.CustomerApi;
import com.securitease.store.entity.Customer;
import com.securitease.store.mapper.CustomerMapper;
import com.securitease.store.model.CreateCustomerRequest;
import com.securitease.store.model.CustomerDTO;
import com.securitease.store.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Override
    @SneakyThrows
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());

    }

    @Override
    public ResponseEntity<List<CustomerDTO>> searchCustomersByName(String name) {
        return ResponseEntity.ok(customerService.searchCustomersByName(name));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.status(201).body(customerMapper.customerToCustomerDTO(customer));
    }
}
