package com.automata.controller.member;

import com.automata.domain.member.Member;
import com.automata.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    public String newOAuthRequest(Model model) {
        return "/member/oauth/new";
    }

    @PostMapping("/oauth/new")
    public String newOAuthRequest(Model model, HttpServletRequest request) {
        OAuth2UserRequest oAuth2UserRequest = memberService.findOAuth2UserBySession(request.getSession());
        OAuth2User member = memberService.login(oAuth2UserRequest);
        memberService.save((Member) member);
        return "redirect:/";
    }
}
