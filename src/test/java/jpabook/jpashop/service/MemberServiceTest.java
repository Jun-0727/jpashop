package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.hibernate.boot.TempTableDdlTransactionHandling;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //Rollback 역할
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memeberRepository;
    // @Autowired EntityManager em; DB에 쿼리문 남는거 보고싶을 때

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memeberRepository.findOne(saveId));
        //em.flush();

    }

    @Test(expected = IllegalStateException.class) // try-catch문 없이 작성할 수 있음
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when

        memberService.join(member1);
        memberService.join(member2);

        /*
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }
        */

        //then
        Assert.fail("예외가 발생해야 한다"); // assertj에서 지원하는 fail이라는 함수. 이코드가 실행되면 안됨
    }
}