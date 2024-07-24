package com.jpaTest.service;

import com.jpaTest.domain.Member;
import com.jpaTest.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    // @Rollback(value = false)  // test 안에선 디폴트가 롤백
    void 회원가입() throws Exception {
        // give
        Member member = new Member();
        member.setUsername("Park");
        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    // @Test(excepted = IllegalStateException.class)
    @Test
    void 중복_회원_예외() throws Exception {
        // give
        Member member1 = new Member();
        member1.setUsername("kim");
        Member member2 = new Member();
        member2.setUsername("kim");
        // when
        memberService.join(member1);
        Assertions.assertThrows(IllegalStateException.class, ()-> memberService.join(member2));

        // memberService.join(member2);

         // then
//        fail("예외 발생해야함");
    }
}