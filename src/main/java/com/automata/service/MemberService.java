package com.automata.service;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberService implements UserDetailsService, OAuth2UserService {

    private Map<HttpSession, OAuth2UserRequest> tempOAuth2Users;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberRepository memberRepository;
    private SecurityContext securityContext = SecurityContextHolder.getContext();

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
        String username = oAuth2User.getAttribute("sub");
        return memberRepository.findByName(username).orElseThrow(() -> {
            saveOAuth2UserRequestAsSession(request.getSession(), userRequest);
            return new UsernameNotFoundException("Could not found user" + username);
        });
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

    public OAuth2UserRequest findOAuth2UserBySession(HttpSession httpSession) {
        return tempOAuth2Users.get(httpSession);
    }

    public OAuth2UserRequest saveOAuth2UserRequestAsSession(HttpSession httpSession, OAuth2UserRequest oAuth2UserRequest) {
        return tempOAuth2Users.put(httpSession, oAuth2UserRequest);
    }

    public OAuth2User login(OAuth2UserRequest userRequest) {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Member member = new Member();
        member.setName(oAuth2User.getAttribute("sub"));
        member.setPassword("pa");
        securityContext.setAuthentication(new OAuth2AuthenticationToken(member, member.getAuthorities(), userRequest.getAccessToken().getTokenValue()));
        return member;
    }
}
