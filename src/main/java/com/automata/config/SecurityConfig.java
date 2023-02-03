package com.automata.config;

import com.automata.repository.MemberRepository;
import com.automata.service.MemberAuthorizationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((requests) -> requests
                        .antMatchers("/", "/index", "/member/new", "/member/oauth/new", "/member/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/member/login")
                        .permitAll()
                )
                .oauth2Login((oauth) -> oauth
                        .loginPage("/member/login")
//                        .defaultSuccessUrl("/member/oauth/new", true)
                        .failureHandler(new MemberAuthorizationFailureHandler())
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