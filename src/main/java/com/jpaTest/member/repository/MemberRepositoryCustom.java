package com.jpaTest.member.repository;

import com.jpaTest.member.dto.MemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberDto> findAllAsDto(); // QueryDSL로 DTO 조회
    MemberDto findByIdAsDto(Long memberId); // ID로 DTO 조회
}