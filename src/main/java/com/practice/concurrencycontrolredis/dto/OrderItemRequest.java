package com.practice.concurrencycontrolredis.dto;

public record OrderItemRequest(Long itemId, Integer quantity) {
}
