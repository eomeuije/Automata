package com.automata.repository.member;

import com.automata.domain.LoginType;
import com.automata.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

    Optional<Member> findByLoginTypeAndOAuthId(LoginType loginType, String oAuthId);
}
