package com.automata.service.security;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberOAuth2UserService extends MemberService implements OAuth2UserService, AuthenticationSuccessHandler {

    private final Map<HttpSession, Authentication> tempOAuth2Users;

    public MemberOAuth2UserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        super(memberRepository, passwordEncoder);
        this.tempOAuth2Users = new ConcurrentHashMap<>();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Member member = new Member();
        member.setName(oAuth2User.getAttribute("sub"));
        return member;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Member member = (Member) authentication.getPrincipal();
        String username = member.getUsername();
        if (memberRepository.findByName(username).isEmpty()) {
            saveOAuth2UserRequestAsSession(request.getSession(), authentication);
            SecurityContextHolder.clearContext();
            response.sendRedirect("/member/oauth/new");
        }else{
            SavedRequest savedRequest = httpSessionRequestCache.getRequest(request, response);
            response.sendRedirect(savedRequest.getRedirectUrl());
        }
    }

    public Authentication findOAuth2UserBySession(HttpSession httpSession) {
        return tempOAuth2Users.get(httpSession);
    }

    public void saveOAuth2UserRequestAsSession(HttpSession httpSession, Authentication authentication) {
        tempOAuth2Users.put(httpSession, authentication);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }
}
