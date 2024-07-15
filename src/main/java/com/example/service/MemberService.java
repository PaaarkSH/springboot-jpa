package com.example.service;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 가입
     * */
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        // exception
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if (!findMembers.isEmpty()) {  // member 수가 0인지를 체크하는게 더 최적화가 된다
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

    }

    // 회원 전체 조회
}
