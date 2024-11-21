package com.jpaTest.member.controller;

import com.jpaTest.member.dto.MemberDto;
import com.jpaTest.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // Member 저장
    @PostMapping
    public String saveMember(@RequestParam String username, @RequestParam int age, @RequestParam Long teamId) {
        memberService.saveMember(username, age, teamId);
        return "Member saved successfully!";
    }

    // 모든 Member 조회
    @GetMapping
    public List<MemberDto> getAllMembers() {
        return memberService.findAllMembersAsDto();
    }

    // 특정 Member 조회 by ID
    @GetMapping("/{id}")
    public MemberDto getMemberById(@PathVariable Long id) {
        return memberService.findMemberById(id);
    }

    // Member 삭제
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "Member deleted successfully!";
    }
}
