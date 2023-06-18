package com.practice.concurrencycontrolredis.application;

import com.practice.concurrencycontrolredis.domain.ItemRepository;
import com.practice.concurrencycontrolredis.domain.Order;
import com.practice.concurrencycontrolredis.domain.OrderItem;
import com.practice.concurrencycontrolredis.domain.OrderRepository;
import com.practice.concurrencycontrolredis.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @ConcurrencyControl(target = "item")
    public Long order(OrderRequest request) {
        List<OrderItem> orderItems = convertToOrderItems(request);
        Order order = Order.create(orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    private List<OrderItem> convertToOrderItems(OrderRequest request) {
        return itemRepository.findAllById(request.extractItemIds())
                .stream()
                .map(item -> new OrderItem(item, request.findQuantity(item.getId())))
                .toList();
    }
}
