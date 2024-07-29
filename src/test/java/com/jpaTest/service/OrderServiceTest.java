package com.jpaTest.service;

import com.jpaTest.domain.Address;
import com.jpaTest.domain.Member;
import com.jpaTest.domain.Order;
import com.jpaTest.domain.OrderStatus;
import com.jpaTest.domain.exception.NotEnoughStockException;
import com.jpaTest.domain.item.Book;
import com.jpaTest.domain.item.Item;
import com.jpaTest.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        // give
        Member member = createMember();

        Book book = createBook("jpa 강의", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());  // "상품 주문시 상태는 ORDER",
        assertEquals(1, getOrder.getOrderItems().size() );  // "종류수가 정확,
        assertEquals(10000 * orderCount, getOrder.getTotalPrice() );  // 주문 가격은 수량 * 10000
        assertEquals(8, book.getStockQuantity());  // 주문 수량만큼 가격이 줄어야함

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // NotEnoughStockException
        // Assertions.assertThrows(IllegalStateException.class, ()-> memberService.join(member2));

        // give
        Member member = createMember();
        Item book = createBook("jpa 강의", 10000, 10);

        int orderCount = 11;

        // when
        //orderService.order(member.getId(), book.getId(), orderCount);
        Assertions.assertThrows(NotEnoughStockException.class, ()-> orderService.order(member.getId(), book.getId(), orderCount));

        // then
        System.out.println("재고 수량 예외가 발생해야함");
    }

    @Test
    public void 주문취소() throws Exception{
        // give
        Member member = createMember();
        Book item = createBook("jpa 강의", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when
        orderService.cancel(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(getOrder.getStatus(), OrderStatus.CANCEL );
        assertEquals(10, item.getStockQuantity());
    }

    @Test
    public void 상푸주문_재고수량초과 () throws Exception{
        // give
        // when
        // then
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("유저1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}