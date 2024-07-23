package com.jpaTest.service;

import com.jpaTest.domain.Delivery;
import com.jpaTest.domain.Member;
import com.jpaTest.domain.Order;
import com.jpaTest.domain.OrderItem;
import com.jpaTest.domain.item.Item;
import com.jpaTest.repository.ItemRepository;
import com.jpaTest.repository.MemberRepository;
import com.jpaTest.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     * */
    @Transactional
    public void cancel(Long orderId) {
        // 취소를 할 때 orderId 만 넘어오게될 때
        // 주문 엔티티를 조회 후 주문 취소
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
    // 검색
/*
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
*/
}
