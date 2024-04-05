package com.automata.service.security;

import com.automata.domain.LoginType;
import com.automata.domain.Member;
import com.automata.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.http.HttpServletRequest;

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

    public Member findByTypeAndId(LoginType loginType, String id) {
        return memberRepository.findByLoginTypeAndOAuthId(loginType, id).orElseThrow(() -> new IllegalArgumentException("Member not found: type-" + loginType + " id-" + id));
    }

    public Member findByName(Member member) {
        return findByName(member.getName());
    }

}
