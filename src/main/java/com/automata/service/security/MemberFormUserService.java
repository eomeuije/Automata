package com.automata.service.security;

import com.automata.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFormUserService extends MemberService implements UserDetailsService {

    public MemberFormUserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        super(memberRepository, passwordEncoder);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user" + username));
    }
}
