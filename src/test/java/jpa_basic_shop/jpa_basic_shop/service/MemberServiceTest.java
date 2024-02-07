package jpa_basic_shop.jpa_basic_shop.service;

import jpa_basic_shop.jpa_basic_shop.domain.Member;
import jpa_basic_shop.jpa_basic_shop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // Given
        Member member = new Member();
        member.setName("yoo");

        // When
        Long savedId = memberService.join(member);

        // Then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test()
    public void 중복_회원_예외() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setName("yoo");

        Member member2 = new Member();
        member2.setName("yoo");

        // When
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException ise) {
            return;
        }

        // Then
        Assertions.fail("이미 존재하는 회원입니다.");
    }
}