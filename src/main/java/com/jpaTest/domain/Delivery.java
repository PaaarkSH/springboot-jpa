package com.jpaTest.domain;

import jakarta.persistence.*;
import jakarta.servlet.UnavailableException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
    /*
    * orders 에 delivery_id 가 있음
    * */
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.ORDINAL)  // 숫자로 들어감
    private DeliveryStatus status;  // ready, comp
}
