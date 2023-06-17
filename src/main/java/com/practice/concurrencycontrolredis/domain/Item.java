package com.practice.concurrencycontrolredis.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Integer stock;

    public Item(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public void minusStock(Integer quantity) {
        if (stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        stock -= quantity;
    }
}
