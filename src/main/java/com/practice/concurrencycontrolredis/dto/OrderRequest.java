package com.practice.concurrencycontrolredis.dto;

import java.util.List;

public record OrderRequest(List<OrderItemRequest> orderItems) {

    public List<Long> extractItemIds() {
        return orderItems.stream()
                .map(OrderItemRequest::itemId)
                .toList();
    }

    public Integer findQuantity(Long itemId) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.itemId().equals(itemId))
                .findAny()
                .orElseThrow()
                .quantity();
    }
}
