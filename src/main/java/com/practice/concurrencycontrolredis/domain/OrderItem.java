package com.practice.concurrencycontrolredis.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;

    public OrderItem(Item item, Integer quantity) {
        this(item, null, quantity);
    }

    public OrderItem(Item item, Order order, Integer quantity) {
        this.item = item;
        this.order = order;
        this.quantity = quantity;
    }

    public void manageStock() {
        item.minusStock(quantity);
    }
}
