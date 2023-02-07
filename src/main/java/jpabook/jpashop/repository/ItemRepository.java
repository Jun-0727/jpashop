package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor // final 변수만 생성자 만들어주는 친구
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {

        if (item.getId() == null) {     // item 값이 없다 --> 새거다
            em.persist(item);
        } else {                        // item 값이 있다 --> 있던거다
            em.merge(item);             // merge : update 느낌
        }

    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
