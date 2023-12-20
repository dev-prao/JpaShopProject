package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("회원가입")
    void joinTest() throws Exception {
        //given
        Member member = new Member();
        member.setName("Park");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    @DisplayName("중복 회원 예외")
    void duplicatedMemberTest() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Park");

        Member member2 = new Member();
        member2.setName("Park");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () ->
                memberService.join(member2)
        );
    }
}
