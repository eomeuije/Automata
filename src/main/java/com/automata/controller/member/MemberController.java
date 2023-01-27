package com.automata.controller.member;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import com.automata.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "/member/login";
    }

    @GetMapping("/new")
    public String newMemberGet(Model model) {
        return "/member/new";
    }

    @PostMapping("/new")
    public String newMemberPost(Member member) {
        memberService.save(member);
        return "redirect:/";
    }

    @GetMapping("/oauth/new")
    public String newOAuth(Model model, @AuthenticationPrincipal Member member) {
        return "/member/oauth/new";
    }

    @GetMapping("/logout")
    public String memberLogoutGet(Model model) {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String memberLogoutPost(Model model) {
        return "redirect:/";
    }
}
