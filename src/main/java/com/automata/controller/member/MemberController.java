package com.automata.controller.member;

import com.automata.domain.member.Member;
import com.automata.service.security.MemberFormUserService;
import com.automata.service.security.MemberOAuth2UserService;
import com.automata.service.security.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberOAuth2UserService memberOAuth2UserService;
    private final MemberFormUserService memberFormUserService;

    @Autowired
    public MemberController(MemberOAuth2UserService memberOAuth2UserService, MemberFormUserService memberFormUserService) {
        this.memberOAuth2UserService = memberOAuth2UserService;
        this.memberFormUserService = memberFormUserService;
    }

    @GetMapping("/login")
    public String login() {
        return "/member/login";
    }

    @GetMapping("/new")
    public String newMemberGet() {
        return "/member/new";
    }

    @PostMapping("/new")
    public String newMemberPost(Member member) {
        memberFormUserService.save(member);
        return "redirect:/";
    }

    @GetMapping("/oauth/new")
    public String newOAuthRequest() {
        return "/member/oauth/new";
    }

    @PostMapping("/oauth/new")
    public String newOAuthRequest(HttpServletRequest request) {
        Authentication authentication = memberOAuth2UserService.findOAuth2UserBySession(request.getSession());
        memberOAuth2UserService.login(authentication);
        memberOAuth2UserService.save((Member) authentication.getPrincipal());
        return "redirect:" + memberOAuth2UserService.getRedirectUrl(request);
    }
}
