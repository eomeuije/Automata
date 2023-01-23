package com.automata.config;

import com.automata.controller.member.OAuth2MemberService;
import com.automata.repository.MemberRepository;
import com.automata.repository.SpringJpaMemberRepository;
import com.automata.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class AutomataConfig {

    public MemberRepository memberRepository;

    public AutomataConfig(SpringJpaMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Bean
    public OAuth2MemberService oAuth2MemberService () {
        return new OAuth2MemberService();
    }
}