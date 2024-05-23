package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.Order;
import com.tericcabrel.authapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
