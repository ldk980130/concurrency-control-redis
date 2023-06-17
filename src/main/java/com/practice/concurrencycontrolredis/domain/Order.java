package com.practice.concurrencycontrolredis.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order create(List<OrderItem> orderItems) {
        orderItems.forEach(OrderItem::manageStock);
        return new Order(orderItems);
    }

    private Order(List<OrderItem> orderItems) {
        this.orderItems.addAll(createWithOrder(orderItems));
    }

    private List<OrderItem> createWithOrder(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItem(orderItem.getItem(), this, orderItem.getQuantity()))
                .toList();
    }
}
