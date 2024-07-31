package com.jpaTest.controller;

import com.jpaTest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String newMember(Model model){
        model.addAttribute("member", new MemberForm());
        return "members/createMemberForm";
    }
}
