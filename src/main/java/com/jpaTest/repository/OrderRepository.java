package com.jpaTest.repository;

import com.jpaTest.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }
/*

    public List<Order> findAll(OrderSearch orderSearch) {
        // Querydsl 적용
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query.
    }
*/

    public List<Order> findAllString(OrderSearch orderSearch) {
        String jpql = """
                 select o
                 from Order o join o.member m
                 where 1=1
                """;
        // 동적 쿼리 조건 추가
        if (orderSearch.getOrderStatus() != null) {
            jpql += " and o.status = :status";
        }
        if (orderSearch.getMemberName() != null && !orderSearch.getMemberName().isEmpty()) {
            jpql += " and m.username like :username";
        }

        // 쿼리 생성
        TypedQuery<Order> query = em.createQuery(jpql, Order.class);

        // 파라미터 설정
        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (orderSearch.getMemberName() != null && !orderSearch.getMemberName().isEmpty()) {
            query.setParameter("username", "%" + orderSearch.getMemberName() + "%");
        }

        // 페이징 처리 (옵션)
        // query.setFirstResult(0);  // 필요한 경우 시작 인덱스 설정
        query.setMaxResults(1000);
        return query.getResultList();
    }

    /**
     * JPA Criteria
     * 동적 쿼리나 파라미터 세팅을 어떻게 하면 더 좋게 사용할 지 JPA 개발자들이 고민하고 만든것
     * 단점: 개발자들이 실무를 해본적이 없는 것 같음
     *  - 가독성이 너무 떨어짐
     * 강사님도 사용 안하심
     *  - 운영할때 써보시다가 유지보수가 안된다고 하심
     * 보여준 이유는 JPA 표준 스펙이라서
     * 살무에서 절대 못씀
     * */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        cq.where(cb.and(criteria.toArray(new Predicate[0])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

}
