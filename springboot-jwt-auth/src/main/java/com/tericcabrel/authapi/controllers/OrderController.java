package com.tericcabrel.authapi.controllers;

import com.tericcabrel.authapi.dtos.CreditCardDto;
import com.tericcabrel.authapi.dtos.OrderDto;
import com.tericcabrel.authapi.dtos.StatusUpdateRequest;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders().stream()
                .map(orderService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.convertToDto(orderService.getOrderById(orderId));
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long orderId, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        OrderDto orderDto = orderService.convertToDto(orderService.updateOrderStatus(orderId, statusUpdateRequest.getStatus()));
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        OrderDto createdOrderDto = orderService.convertToDto(orderService.createOrder(currentUser, orderDto));
        return new ResponseEntity<>(createdOrderDto, HttpStatus.CREATED);
    }


    @GetMapping("/me")
    public ResponseEntity<List<OrderDto>> getOrdersByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());
        List<OrderDto> orderDtos = orders.stream().map(orderService::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }
}
