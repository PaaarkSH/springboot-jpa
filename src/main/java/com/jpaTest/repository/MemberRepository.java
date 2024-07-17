package com.jpaTest.repository;

import com.jpaTest.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository  {
    // @PersistenceContext  // 스프링 부트에서 생략 가능
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);  // 저장하는 로직
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.username = :name ", Member.class)
                .setParameter("name", name)
                .getResultList()
        ;
    }
}
