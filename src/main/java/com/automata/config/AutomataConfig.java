package com.automata.config;

import com.automata.repository.item.ItemRepository;
import com.automata.repository.member.MemberRepository;
import com.automata.service.security.MemberFormUserService;
import com.automata.service.security.MemberOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AutomataConfig {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AutomataConfig(MemberRepository memberRepository, ItemRepository itemRepository) {
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MemberFormUserService memberFormUserService() {
        return new MemberFormUserService(memberRepository, passwordEncoder);
    }

    @Bean
    public MemberOAuth2UserService memberOAuth2UserService() {
        return new MemberOAuth2UserService(memberRepository, passwordEncoder);
    }
}