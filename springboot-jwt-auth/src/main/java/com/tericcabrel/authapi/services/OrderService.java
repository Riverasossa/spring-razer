package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.OrderDto;
import com.tericcabrel.authapi.entities.*;
import com.tericcabrel.authapi.repositories.OrderRepository;
import com.tericcabrel.authapi.repositories.ShoppingCartRepository;
import com.tericcabrel.authapi.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.getOrderProducts().size(); // Initialize the products collection
            return order;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public Order createOrder(User user, OrderDto orderDto) {
        ShoppingCart shoppingCart = user.getShoppingCart();
        if (shoppingCart == null || shoppingCart.getShoppingCartProducts().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        Hibernate.initialize(shoppingCart.getShoppingCartProducts());

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING); // Set the initial order status
        order.setAddress(orderDto.getAddress());
        order.setAddress2(orderDto.getAddress2());
        order.setProvince(orderDto.getProvince());
        order.setCanton(orderDto.getCanton());
        order.setDistrict(orderDto.getDistrict());
        order.setZipCode(orderDto.getZipCode());
        order.setCard(orderDto.getCard());
        order.setSubtotal(shoppingCart.getTotal());
        order.setShippingCoast(9.99);
        order.setTaxes(0.0);
        order.setTotal(order.getSubtotal() + order.getShippingCoast() + order.getTaxes());

        Set<OrderProduct> orderProducts = new HashSet<>();
        shoppingCart.getShoppingCartProducts().forEach(cartProduct -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProducts.add(orderProduct);
        });
        order.setOrderProducts(orderProducts);

        orderRepository.save(order);

        shoppingCart.getShoppingCartProducts().clear();
        shoppingCartRepository.save(shoppingCart);

        return order;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);
            return order;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
