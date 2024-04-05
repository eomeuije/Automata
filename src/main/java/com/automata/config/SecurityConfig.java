package com.automata.config;

import com.automata.service.security.MemberFormUserService;
import com.automata.service.security.MemberOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    MemberOAuth2UserService memberOAuth2UserService;
    MemberFormUserService memberFormUserService;

    public SecurityConfig(MemberOAuth2UserService memberOAuth2UserService, MemberFormUserService memberFormUserService) {
        this.memberOAuth2UserService = memberOAuth2UserService;
        this.memberFormUserService = memberFormUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/"
                                , "/index"
                                , "/member/new"
                                , "/member/oauth/new/continue"
                                , "/member/oauth/new"
                                , "/member/logout"
                                , "/item/s"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/member/login")
                        .failureUrl("/member/login?error=true")
                        .successHandler(memberFormUserService)
                        .permitAll()
                )
                .oauth2Login((oauth) -> oauth
                        .loginPage("/member/login")
                        .successHandler(memberOAuth2UserService)
                        .permitAll()
                )
                .logout((out) -> out
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

}