package com.tericcabrel.authapi.dtos;

import com.tericcabrel.authapi.entities.OrderStatus;

public class StatusUpdateRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
