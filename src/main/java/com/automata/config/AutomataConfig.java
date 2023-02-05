package com.automata.config;

import com.automata.repository.MemberRepository;
import com.automata.repository.SpringJpaMemberRepository;
import com.automata.service.security.MemberFormUserService;
import com.automata.service.security.MemberOAuth2UserService;
import com.automata.service.security.MemberService;
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
    public MemberFormUserService memberFormUserService() {
        return new MemberFormUserService(memberRepository, PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Bean
    public MemberOAuth2UserService memberOAuth2UserService() {
        return new MemberOAuth2UserService(memberRepository, PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }
}