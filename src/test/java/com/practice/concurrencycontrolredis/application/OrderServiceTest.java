package com.practice.concurrencycontrolredis.application;

import com.practice.concurrencycontrolredis.domain.Item;
import com.practice.concurrencycontrolredis.domain.ItemRepository;
import com.practice.concurrencycontrolredis.domain.OrderRepository;
import com.practice.concurrencycontrolredis.dto.OrderItemRequest;
import com.practice.concurrencycontrolredis.dto.OrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Long spicyChickenId;
    private Long friedChickenId;
    private Long cheeseBallId;

    @BeforeEach
    void init() {
        Item spicyChicken = new Item("양념 치킨", 50);
        Item friedChicken = new Item("후라이드 치킨", 50);
        Item cheeseBall = new Item("치즈볼", 100);

        spicyChickenId = itemRepository.save(spicyChicken).getId();
        friedChickenId = itemRepository.save(friedChicken).getId();
        cheeseBallId = itemRepository.save(cheeseBall).getId();

        System.out.println("====테스트 시작====");
    }

    @AfterEach
    void clear() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void 하나의_주문을_처리한다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(spicyChickenId, 10),
                new OrderItemRequest(friedChickenId, 5),
                new OrderItemRequest(cheeseBallId, 3))
        );

        // when
        Long orderId = orderService.order(orderRequest);

        // then
        Integer spicyStock = itemRepository.findById(spicyChickenId).orElseThrow().getStock();
        Integer friedStock = itemRepository.findById(friedChickenId).orElseThrow().getStock();
        Integer ballStock = itemRepository.findById(cheeseBallId).orElseThrow().getStock();
        assertAll(
                () -> assertThat(orderId).isNotNull(),
                () -> assertThat(spicyStock).isEqualTo(40),
                () -> assertThat(friedStock).isEqualTo(45),
                () -> assertThat(ballStock).isEqualTo(97)
        );

    }
}
