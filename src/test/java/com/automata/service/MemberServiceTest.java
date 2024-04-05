package com.automata.service;

import com.automata.domain.Member;
import com.automata.service.security.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberFormUserService;

    @Test
    public void findOne() {
        Member member = new Member();
        member.setName("automata");
        member.setPassword("password");
        memberFormUserService.save(member);
        Member found = memberFormUserService.findByName("automata");
        Assertions.assertThat(member).isEqualTo(found);
    }
}