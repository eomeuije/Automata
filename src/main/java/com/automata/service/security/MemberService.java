package com.automata.service.security;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

public class MemberService {

    protected final HttpSessionRequestCache httpSessionRequestCache = new HttpSessionRequestCache();
    protected final PasswordEncoder passwordEncoder;
    protected final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("Member not found: " + name));
    }

    public Member findByName(Member member) {
        return findByName(member.getName());
    }

    public void login(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String getRedirectUrl(HttpServletRequest request) {
        SavedRequest savedRequest = httpSessionRequestCache.getRequest(request, null);
        return savedRequest.getRedirectUrl();
    }

}
