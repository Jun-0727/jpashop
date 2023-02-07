package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //JPA의 모든 데이터 변경이나 로직들은 가급적이면 @Transactional 안에서 실행되어야함 / spring에서 제공하는걸로 쓰자. (javax아님)
// @AllArgsConstructor 알아서 생성자 만들어준다
@RequiredArgsConstructor // final 애들만 생성자를 만들어준다
public class MemberService {


    private final MemberRepository memberRepository;

    /*
    //@Autowired
    // 생성자가 하나만 있는경우 @Autowired 안달아도 스프링이 알아서 스프링빈에 등록 해준다
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    // 회원가입
    @Transactional // readOnly 는 default가 false. 이렇게 안에서 쓰면 이게 우선순위 transaction이 됨
    public Long join(Member member) {

        validateDuplicateMember(member);// 중복 회원 검증
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {

        // 동시에 가입할수도 있잖아 (MultiThread)
        // 실무에서는 member에 유니크 제약조건을 걸어서 쓰면 안전하대
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }
}
