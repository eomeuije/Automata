package com.automata.service.security;

import com.automata.domain.LoginType;
import com.automata.domain.Member;
import com.automata.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemberOAuth2UserService extends MemberService implements OAuth2UserService, AuthenticationSuccessHandler {

    private final Map<String, Authentication> tempOAuth2Users;
    private final Map<Date, String> sessionTimestamp;

    @Scheduled(fixedRate = 10000)
    public void removeTimeoutAuthenticationFromMap() {
        long currentDate = new Date().getTime();
        Iterator<Map.Entry<Date, String>> iterator = sessionTimestamp.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Date, String> entry = iterator.next();
            Date timestamp = entry.getKey();

            // 회원가입 대기중인 세션이 10분이 넘어가면 삭제
            if (currentDate - timestamp.getTime() > 600000) {
                iterator.remove(); // Iterator를 사용하여 안전하게 요소를 삭제
                tempOAuth2Users.remove(entry.getValue());
            } else {
                return;
            }
        }
    }

    public MemberOAuth2UserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        super(memberRepository, passwordEncoder);
        this.tempOAuth2Users = new ConcurrentHashMap<>();
        this.sessionTimestamp = new LinkedHashMap<>();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String oAuthId = oAuth2User.getAttribute("sub");
        LoginType loginType = LoginType.valueOf(registrationId.toUpperCase());

        Member member = new Member();
        member.setOAuthId(oAuthId);
        member.setLoginType(loginType);
        member.setName(oAuth2User.getAttribute("name"));
        member.setNickName(oAuth2User.getAttribute("given_name"));
        member.setEmail(oAuth2User.getAttribute("email"));
        return member;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Member member = (Member) authentication.getPrincipal();
        String oAuthId = member.getOAuthId();
        LoginType loginType = member.getLoginType();
        Optional<Member> foundMember = memberRepository.findByLoginTypeAndOAuthId(loginType, oAuthId);
        // 없는 계정일때 자동으로 회원가입 페이지로
        if (foundMember.isEmpty()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            String sessionId = request.getSession().getId();
            saveOAuth2UserRequestAsSession(sessionId, authentication);
            response.sendRedirect("/member/oauth/new");
        }else{
            member.setId(foundMember.get().getId());
            Optional<SavedRequest> savedRequest = Optional.ofNullable(httpSessionRequestCache.getRequest(request, response));
            if (savedRequest.isEmpty()) {
                response.sendRedirect("/");
            } else {
                String redirectUrl = savedRequest.get().getRedirectUrl();
                if (redirectUrl.contains("/member/login")) {
                    response.sendRedirect("/");
                } else {
                    response.sendRedirect(redirectUrl);
                }
            }
        }
    }

    public Authentication removeOAuth2UserFromSession(String sessionId) {
        return tempOAuth2Users.remove(sessionId);
    }

    public void saveOAuth2UserRequestAsSession(String sessionId, Authentication authentication) {
        tempOAuth2Users.put(sessionId, authentication);
        sessionTimestamp.put(new Date(), sessionId);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    public void login(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContext securityContext = new SecurityContext() {
            Authentication authentication;

            @Override
            public Authentication getAuthentication() {
                return authentication;
            }

            @Override
            public void setAuthentication(Authentication authentication) {
                this.authentication = authentication;
            }
        };
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        new HttpSessionSecurityContextRepository().saveContext(securityContext, request, response);
    }
}
