package com.securitease.store.service;

import com.securitease.store.entity.Customer;
import com.securitease.store.mapper.CustomerMapper;
import com.securitease.store.model.CreateCustomerRequest;
import com.securitease.store.model.CustomerDTO;
import com.securitease.store.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_WhenCustomersExist_ShouldReturnAllCustomerDTOs() {

        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(String.valueOf(1L));
        customerDTO1.setName("John Doe");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setId(String.valueOf(2L));
        customerDTO2.setName("Jane Smith");

        List<CustomerDTO> expectedDTOs = Arrays.asList(customerDTO1, customerDTO2);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(expectedDTOs);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDTOs, result);
        verify(customerRepository).findAll();
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void getAllCustomers_WhenNoCustomersExist_ShouldReturnEmptyList() {

        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        when(customerMapper.customersToCustomerDTOs(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository).findAll();
        verify(customerMapper).customersToCustomerDTOs(Collections.emptyList());
    }

    @Test
    void searchCustomersByName_WhenMatchingCustomersExist_ShouldReturnMatchingCustomerDTOs() {

        String searchName = "John";

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        List<Customer> customers = Collections.singletonList(customer);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(String.valueOf(1L));
        customerDTO.setName("John Doe");
        List<CustomerDTO> expectedDTOs = Collections.singletonList(customerDTO);

        when(customerRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(expectedDTOs);

        List<CustomerDTO> result = customerService.searchCustomersByName(searchName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDTOs, result);
        verify(customerRepository).findByNameContainingIgnoreCase(searchName);
        verify(customerMapper).customersToCustomerDTOs(customers);
    }

    @Test
    void searchCustomersByName_WhenNoMatchingCustomers_ShouldReturnEmptyList() {
        String searchName = "XYZ";
        when(customerRepository.findByNameContainingIgnoreCase(searchName))
                .thenReturn(Collections.emptyList());
        when(customerMapper.customersToCustomerDTOs(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        List<CustomerDTO> result = customerService.searchCustomersByName(searchName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository).findByNameContainingIgnoreCase(searchName);
        verify(customerMapper).customersToCustomerDTOs(Collections.emptyList());
    }

    @Test
    void createCustomer_WhenValidRequest_ShouldReturnCreatedCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("John Doe");

        Customer expectedCustomer = new Customer();
        expectedCustomer.setName("John Doe");
        expectedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        Customer result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals(expectedCustomer.getId(), result.getId());
        assertEquals(expectedCustomer.getName(), result.getName());

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_WhenNullName_ShouldHandleNullInput() {

        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName(null);

        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        Customer result = customerService.createCustomer(request);

        assertNotNull(result);
        assertNull(result.getName());
        verify(customerRepository).save(any(Customer.class));
    }

}