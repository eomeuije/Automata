package com.automata.controller.member;

import com.automata.domain.LoginType;
import com.automata.domain.Member;
import com.automata.service.security.MemberFormUserService;
import com.automata.service.security.MemberOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

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

    @GetMapping("/info")
    public String info(Model model, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        model.addAttribute("member", member);
        return "/member/info";
    }

    @GetMapping("/new")
    public String newMemberGet() {
        return "/member/new";
    }

    @PostMapping("/new")
    public String newMemberPost(Member member) {
        member.setLoginType(LoginType.FORM);
        memberFormUserService.save(member);
        return "redirect:/member/login";
    }

    @GetMapping("/oauth/new")
    public String newOAuthRequest() {
        return "/member/oauth/new";
    }

    @GetMapping("/oauth/new/continue")
    public String newOAuthRequest(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        Authentication authentication = memberOAuth2UserService.removeOAuth2UserFromSession(sessionId);
        memberOAuth2UserService.save((Member) authentication.getPrincipal());
        memberOAuth2UserService.login(request, response, authentication);
        return "redirect:/";
    }
}
