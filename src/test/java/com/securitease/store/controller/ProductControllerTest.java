package com.securitease.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securitease.store.model.CreateProductRequest;
import com.securitease.store.model.ProductDTO;
import com.securitease.store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Mock response
        ProductDTO product1 = new ProductDTO();
        product1.setId("1");
        product1.setName("Product A");
        product1.setPrice(Double.valueOf("100.0"));
        product1.setOrderIds(List.of("101", "102"));

        ProductDTO product2 = new ProductDTO();
        product2.setId("2");
        product2.setName("Product B");
        product2.setPrice(Double.valueOf("200.0"));
        product2.setOrderIds(List.of("103"));

        List<ProductDTO> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/v1/api/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDTO product1 = new ProductDTO();
        product1.setId("1");
        product1.setName("Product A");
        product1.setPrice(Double.valueOf("100.0"));
        product1.setOrderIds(List.of("101", "102"));

        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/v1/api/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product A"))
                .andExpect(jsonPath("$.orderIds[0]").value("101"));
    }

    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setName("New Product");
        createProductRequest.setPrice(200.0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("1");
        productDTO.setName("New Product");
        productDTO.setPrice(200.0);

        when(productService.createProduct(createProductRequest)).thenReturn(productDTO);

        mockMvc.perform(post("/v1/api//product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(200.0));

        verify(productService, times(1)).createProduct(createProductRequest);
    }
}
