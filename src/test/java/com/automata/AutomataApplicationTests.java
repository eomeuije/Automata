package com.automata;

import com.automata.domain.Member;
import com.automata.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class AutomataApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void findAll() {
		Member member1 = new Member();
		member1.setName("Spring1");
		member1.setPassword("password");
		memberRepository.save(member1);

		Member member2 = new Member();
		member2.setName("Spring2");
		member2.setPassword("password");
		memberRepository.save(member2);

		List<Member> result = memberRepository.findAll();

		Assertions.assertThat(result.size()).isEqualTo(2);
	}
}
