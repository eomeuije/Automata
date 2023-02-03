package com.automata.service;

import com.automata.domain.member.Member;
import com.automata.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findOne() {
        Member member = new Member();
        member.setName("automata");
        member.setNickName("autoNick");
        member.setPassword("password");
        memberService.save(member);
        Member found = memberService.findByName("automata");
        Assertions.assertThat(member).isEqualTo(found);
    }
}