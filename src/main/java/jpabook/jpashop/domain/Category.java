package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns =  @JoinColumn(name = "item_id")) // 중간 테이블 매핑을 해줘야함.  JPA에서 다대다  -->  1대다-다대1.. 근데 쓰지말
    private List<Item> items = new ArrayList<>();


    // 카테고리 구조, 계층구조 어떻게하지?
    // 부모가 타입이니까 Category 로 넣어주고
    // 부모니까 ManyToOne이겠지? --> JoinColumn()..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    // 자식은 여러개 가질 수 있잖여
    // mappedBy에 부모 넣어주면 됨
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);

    }
}
