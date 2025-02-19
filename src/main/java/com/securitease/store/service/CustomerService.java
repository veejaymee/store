package com.securitease.store.service;

import com.securitease.store.entity.Customer;
import com.securitease.store.mapper.CustomerMapper;
import com.securitease.store.model.CreateCustomerRequest;
import com.securitease.store.model.CustomerDTO;
import com.securitease.store.repository.CustomerRepository;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @SneakyThrows
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.customersToCustomerDTOs(customers);
    }

    @SneakyThrows
    public List<CustomerDTO> searchCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customerMapper.customersToCustomerDTOs(customers);
    }

    @SneakyThrows
    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();
        customer.setName(createCustomerRequest.getName());
        return customerRepository.save(customer);
    }
}
