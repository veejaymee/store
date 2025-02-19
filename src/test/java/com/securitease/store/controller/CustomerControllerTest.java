package com.securitease.store.controller;

import com.securitease.store.entity.Customer;
import com.securitease.store.mapper.CustomerMapper;
import com.securitease.store.model.CreateCustomerRequest;
import com.securitease.store.model.CustomerDTO;
import com.securitease.store.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testGetAllCustomers_Success() {
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId("1");
        customerDTO1.setName("John Doe");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setId("2");
        customerDTO2.setName("John Doe");

        List<CustomerDTO> mockCustomers = Arrays.asList(customerDTO1, customerDTO2);

        when(customerService.getAllCustomers()).thenReturn(mockCustomers);

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCustomers, response.getBody());
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void testSearchCustomersByName_Success() {
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId("1");
        customerDTO1.setName("John Doe");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setId("2");
        customerDTO2.setName("John Doe");

        String name = "Doe";
        List<CustomerDTO> mockCustomers = Arrays.asList(customerDTO1, customerDTO2);

        when(customerService.searchCustomersByName(name)).thenReturn(mockCustomers);

        ResponseEntity<List<CustomerDTO>> response = customerController.searchCustomersByName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCustomers, response.getBody());
        verify(customerService, times(1)).searchCustomersByName(name);
    }

    @Test
    public void testSearchCustomersByName_NoResults() {

        String name = "Unknown";
        when(customerService.searchCustomersByName(name)).thenReturn(Collections.emptyList());

        ResponseEntity<List<CustomerDTO>> response = customerController.searchCustomersByName(name);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(customerService, times(1)).searchCustomersByName(name);
    }

    @Test
    public void testCreateCustomer_Success() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setName("John Doe");

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("John Doe");

        CustomerDTO mockCustomerDTO = new CustomerDTO();
        mockCustomerDTO.setId("1");
        mockCustomerDTO.setName("John Doe");
        when(customerService.createCustomer(request)).thenReturn(mockCustomer);
        when(customerMapper.customerToCustomerDTO(mockCustomer)).thenReturn(mockCustomerDTO);

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockCustomerDTO, response.getBody());
        verify(customerService, times(1)).createCustomer(request);
        verify(customerMapper, times(1)).customerToCustomerDTO(mockCustomer);
    }
}