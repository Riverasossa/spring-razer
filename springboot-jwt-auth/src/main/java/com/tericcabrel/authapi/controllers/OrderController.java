package com.tericcabrel.authapi.controllers;

import com.tericcabrel.authapi.dtos.OrderDto;
import com.tericcabrel.authapi.entities.Order;
import com.tericcabrel.authapi.entities.OrderStatus;
import com.tericcabrel.authapi.entities.User;
import com.tericcabrel.authapi.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Order order = orderService.createOrder(currentUser, orderDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Order>> getOrdersByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
