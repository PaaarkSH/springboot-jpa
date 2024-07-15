package com.example.service;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
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

    @Test
    void 회원가입() throws Exception {
        // give
        Member member = new Member();
        member.setUsername("Park");
        // when

        // then
    }

    @Test
    void 중복회원예외() throws Exception {
        // give
        // when
        // then
    }
}