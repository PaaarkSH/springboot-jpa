package com.jpaTest.member.service;


import com.jpaTest.member.dto.MemberDto;
import com.jpaTest.member.entity.Member;
import com.jpaTest.member.repository.MemberJpaRepository;
import com.jpaTest.team.entity.Team;
import com.jpaTest.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberJpaRepository memberRepository;
    private final TeamRepository teamRepository;

    // Member 저장
    public void saveMember(String username, int age, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        Member member = new Member(username, age, team);
        memberRepository.save(member);
    }

    // 모든 Member 조회 (DTO 변환)
    public List<MemberDto> findAllMembersAsDto() {
        return memberRepository.findAllAsDto();
    }

    // Member 조회 by ID (DTO 변환)
    public MemberDto findMemberById(Long memberId) {
        return memberRepository.findByIdAsDto(memberId);
    }

    // Member 삭제
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}