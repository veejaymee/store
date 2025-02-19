package com.securitease.store.service;

import com.securitease.store.entity.Customer;
import com.securitease.store.entity.Order;
import com.securitease.store.entity.OrderProduct;
import com.securitease.store.entity.Product;
import com.securitease.store.mapper.OrderMapper;
import com.securitease.store.model.CreateOrderRequest;
import com.securitease.store.model.OrderDTO;
import com.securitease.store.model.OrderProductDTO;
import com.securitease.store.model.ProductRequest;
import com.securitease.store.repository.CustomerRepository;
import com.securitease.store.repository.OrderRepository;
import com.securitease.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_WhenValidRequest_ShouldReturnOrderDTO() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("1");
        request.setDescription("Test Order");

        ProductRequest productRequest1 = new ProductRequest();
        productRequest1.setProductId("1");
        ProductRequest productRequest2 = new ProductRequest();
        productRequest2.setProductId("2");
        request.setProductIds(Arrays.asList(productRequest1, productRequest2));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setDescription("Test Order");
        savedOrder.setCustomer(customer);
        savedOrder.setProducts(Arrays.asList(product1, product2));

        OrderDTO expectedOrderDTO = new OrderDTO();
        expectedOrderDTO.setId("1");
        expectedOrderDTO.setDescription("Test Order");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDTO(savedOrder)).thenReturn(expectedOrderDTO);

        OrderDTO result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(expectedOrderDTO.getId(), result.getId());
        assertEquals(expectedOrderDTO.getDescription(), result.getDescription());

        verify(customerRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(productRepository).findById(2L);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).orderToOrderDTO(savedOrder);
    }

    @Test
    void createOrder_WhenCustomerNotFound_ShouldThrowException() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("1");
        request.setDescription("Test Order");
        request.setProductIds(Collections.emptyList());

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository).findById(1L);
        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void createOrder_WhenProductNotFound_ShouldThrowException() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("1");
        request.setDescription("Test Order");

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId("1");
        request.setProductIds(Collections.singletonList(productRequest));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals("Product not found", exception.getMessage());
        verify(customerRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).orderToOrderDTO(any());
    }


    @Test
    void getAllOrders_WhenOrdersExist_ShouldReturnPagedOrderDTOs() {
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity(2);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity(3);

        Order order1 = new Order();
        order1.setId(1L);
        order1.setDescription("Order 1");
        order1.setCustomer(customer);
        order1.setOrderProducts(Arrays.asList(orderProduct1));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setDescription("Order 2");
        order2.setCustomer(customer);
        order2.setOrderProducts(Arrays.asList(orderProduct2));

        List<Order> orders = Arrays.asList(order1, order2);
        Page<Order> orderPage = new PageImpl<>(orders, pageable, 4); // 4 is total elements

        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId("1");
        orderDTO1.setDescription("Order 1");
        orderDTO1.setCustomerId("1");
        orderDTO1.setProducts(Collections.singletonList(
                new OrderProductDTO("1", 2)
        ));

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setId("2");
        orderDTO2.setDescription("Order 2");
        orderDTO2.setCustomerId("1");
        orderDTO2.setProducts(Collections.singletonList(
                new OrderProductDTO("2", 3)
        ));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.orderToOrderDTO(order1)).thenReturn(orderDTO1);
        when(orderMapper.orderToOrderDTO(order2)).thenReturn(orderDTO2);

        Page<OrderDTO> result = orderService.getAllOrders(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(4, result.getTotalElements());
        assertEquals(2, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getTotalPages());

        OrderDTO firstOrder = result.getContent().get(0);
        assertEquals("1", firstOrder.getId());
        assertEquals("1", firstOrder.getCustomerId());
        assertEquals(1, firstOrder.getProducts().size());
        assertEquals("1", firstOrder.getProducts().get(0).getProductId());
        assertEquals(2, firstOrder.getProducts().get(0).getQuantity());

        OrderDTO secondOrder = result.getContent().get(1);
        assertEquals("2", secondOrder.getId());
        assertEquals("1", secondOrder.getCustomerId());
        assertEquals(1, secondOrder.getProducts().size());
        assertEquals("2", secondOrder.getProducts().get(0).getProductId());
        assertEquals(3, secondOrder.getProducts().get(0).getQuantity());

        verify(orderRepository).findAll(pageable);
        verify(orderMapper).orderToOrderDTO(order1);
        verify(orderMapper).orderToOrderDTO(order2);
    }

    @Test
    void getAllOrders_WhenNoOrders_ShouldReturnEmptyPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(orderRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<OrderDTO> result = orderService.getAllOrders(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(orderRepository).findAll(pageable);
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrderDTO() {

        Long orderId = 1L;

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1);
        orderProduct1.setQuantity(2);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProduct(product2);
        orderProduct2.setQuantity(3);

        Order order = new Order();
        order.setId(orderId);
        order.setDescription("Test Order");
        order.setCustomer(customer);
        order.setOrderProducts(Arrays.asList(orderProduct1, orderProduct2));

        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setId(orderId.toString());
        expectedDTO.setDescription("Test Order");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(expectedDTO);

        Optional<OrderDTO> result = orderService.getOrderById(orderId);

        assertTrue(result.isPresent());
        OrderDTO orderDTO = result.get();
        assertEquals(orderId.toString(), orderDTO.getId());
        assertEquals("1", orderDTO.getCustomerId());
        assertEquals(2, orderDTO.getProducts().size());

        OrderProductDTO firstProduct = orderDTO.getProducts().get(0);
        assertEquals("1", firstProduct.getProductId());
        assertEquals(2, firstProduct.getQuantity());

        OrderProductDTO secondProduct = orderDTO.getProducts().get(1);
        assertEquals("2", secondProduct.getProductId());
        assertEquals(3, secondProduct.getQuantity());

        verify(orderRepository).findById(orderId);
        verify(orderMapper).orderToOrderDTO(order);
    }

    @Test
    void getOrderById_WhenOrderDoesNotExist_ShouldReturnEmptyOptional() {

        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<OrderDTO> result = orderService.getOrderById(orderId);

        assertFalse(result.isPresent());
        verify(orderRepository).findById(orderId);
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void getOrderById_WhenOrderIdIsNull_ShouldReturnEmptyOptional() {

        Optional<OrderDTO> result = orderService.getOrderById(null);

        assertFalse(result.isPresent());
        verify(orderRepository).findById(null);
        verify(orderMapper, never()).orderToOrderDTO(any());
    }

    @Test
    void getOrderById_WhenCustomerIsNull_ShouldThrowException() {

        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setDescription("Test Order");
        order.setCustomer(null);

        OrderDTO expectedDTO = new OrderDTO();
        expectedDTO.setId(orderId.toString());
        expectedDTO.setDescription("Test Order");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(expectedDTO);

        assertThrows(
                NullPointerException.class,
                () -> orderService.getOrderById(orderId)
        );

        verify(orderRepository).findById(orderId);
        verify(orderMapper).orderToOrderDTO(order);
    }
}