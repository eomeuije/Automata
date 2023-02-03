package com.automata.service;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberService implements UserDetailsService, OAuth2UserService, AuthenticationSuccessHandler {

    private final Map<HttpSession, Authentication> tempOAuth2Users;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.tempOAuth2Users = new ConcurrentHashMap<>();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Member member = new Member();
        member.setName(oAuth2User.getAttribute("sub"));
        member.setPassword("pa");
        return member;
    }

    public Member save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return member;
    }

    public Member findByName(String name) {
        return (Member) loadUserByUsername(name);
    }

    public Member findByName(Member member) {
        return findByName(member.getName());
    }

    public Authentication findOAuth2UserBySession(HttpSession httpSession) {
        return tempOAuth2Users.get(httpSession);
    }

    public void saveOAuth2UserRequestAsSession(HttpSession httpSession, Authentication authentication) {
        tempOAuth2Users.put(httpSession, authentication);
    }

    public Authentication saveOAuth2UserRequestAsSession(Authentication authentication) {
        return tempOAuth2Users.put(request.getSession(), authentication);
    }

    public void login(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
            response.sendRedirect("/");
        }
    }
}
