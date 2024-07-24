package com.jpaTest.service;

import com.jpaTest.domain.Address;
import com.jpaTest.domain.Member;
import com.jpaTest.domain.Order;
import com.jpaTest.domain.OrderStatus;
import com.jpaTest.domain.item.Book;
import com.jpaTest.repository.OrderRepository;
import jakarta.persistence.EntityManager;
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

        Book book = createBook();

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
        // when
        // then
    }

    @Test
    public void 주문취소() throws Exception{
        // give
        // when
        // then
    }

    @Test
    public void 상푸주문_재고수량초과 () throws Exception{
        // give
        // when
        // then
    }

    private Book createBook() {
        Book book = new Book();
        book.setName("jpa 강의");
        book.setPrice(10000);
        book.setStockQuantity(10);
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