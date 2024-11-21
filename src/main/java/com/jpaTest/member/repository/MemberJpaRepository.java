package com.jpaTest.member.repository;

import com.jpaTest.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    // spring data jpa 가 기본 제공
    List<Member> findByUsername(String username);
}
