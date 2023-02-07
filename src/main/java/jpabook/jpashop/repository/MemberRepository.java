package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.plaf.metal.MetalMenuBarUI;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
    // 1. 원래 이렇게 하던걸
    @PersistenceContext
    private EntityManager em;

    // 2. 이렇게 해도 되는데
    // 이건 스프링 부트가 지원하는 기능
    @Autowired
    private EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }
    */

    // 3. 결론은 여기에 @RequiredArgsConstructor 이거 붙여서 쓸거임.
    // EntityManger를 Injection해서 써야한다
    // 원래는 @PersistenceContext 이걸로 해야되는데
    // 스프링부트가 생성자 만들어서 @Autowired 붙이는 것도 지원해줌
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // SQL과 차이가 있다. SQL은 개체에 대한 쿼리
        // JPQL은 Entity에 대한 쿼리.. 기본편을 들어야될
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        // option + command + N : inline 단축키
        return result;
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
