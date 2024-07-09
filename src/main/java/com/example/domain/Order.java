package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    /*
    * member_id (fk)
    * delivery_id  (fk)
    * orderdate
    * status
    * */

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 즉시  로딩
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;  // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;  // 주문상태
}
