package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.*;
import com.tericcabrel.authapi.entities.*;
import com.tericcabrel.authapi.repositories.OrderRepository;
import com.tericcabrel.authapi.repositories.ShoppingCartRepository;
import com.tericcabrel.authapi.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Transactional
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> Hibernate.initialize(order.getOrderProducts()));
        return orders;
    }

    @Transactional
    public Order getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Hibernate.initialize(order.getOrderProducts());
            return order;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public Order createOrder(User user, OrderDto orderDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIdWithProducts(user.getId());
        if (shoppingCart == null || shoppingCart.getShoppingCartProducts().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setAddress(orderDto.getAddress());
        order.setAddress2(orderDto.getAddress2());
        order.setProvince(orderDto.getProvince());
        order.setCanton(orderDto.getCanton());
        order.setDistrict(orderDto.getDistrict());
        order.setZipCode(orderDto.getZipCode());

        CreditCardDto creditCardDto = orderDto.getCreditCard();
        order.setCard(creditCardDto.getCardNumber().substring(creditCardDto.getCardNumber().length() - 4));

        order.setSubtotal(shoppingCart.getTotal());
        order.setShippingCoast(9.99);
        order.setTaxes(order.getSubtotal() * 0.13);
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
        shoppingCartService.clearCart(user.getId());

        return order;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Hibernate.initialize(order.getOrderProducts());
            order.setStatus(status);
            orderRepository.save(order);
            return order;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public List<Order> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        orders.forEach(order -> Hibernate.initialize(order.getOrderProducts()));
        return orders;
    }

    public OrderDto convertToDto(Order order) {
        UserDto userDto = new UserDto(order.getUser().getFullName(), order.getUser().getEmail());

        BigDecimal total = BigDecimal.valueOf(order.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal taxes = BigDecimal.valueOf(order.getTaxes()).setScale(2, BigDecimal.ROUND_HALF_UP);

        Set<OrderProductDto> orderProductDtos = order.getOrderProducts().stream()
                .map(orderProduct -> new OrderProductDto(orderProduct.getOrderProductId(), new ProductDto(orderProduct.getProduct()
                                        .getProductId(), orderProduct.getProduct().getName(), orderProduct.getProduct().getPrice()),
                                        orderProduct.getQuantity()))
                .collect(Collectors.toSet());

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setUser(userDto);
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setAddress(order.getAddress());
        orderDto.setAddress2(order.getAddress2());
        orderDto.setProvince(order.getProvince());
        orderDto.setCanton(order.getCanton());
        orderDto.setDistrict(order.getDistrict());
        orderDto.setZipCode(order.getZipCode());
        orderDto.setCard(order.getCard());
        orderDto.setSubtotal(order.getSubtotal());
        orderDto.setShippingCoast(order.getShippingCoast());
        orderDto.setTaxes(taxes.doubleValue());
        orderDto.setTotal(total.doubleValue());
        orderDto.setOrderProducts(orderProductDtos);

        return orderDto;
    }

}
