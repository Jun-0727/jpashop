package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계 매핑이기 때문에 상속관계 전략을 지정해야되는데 이 전략을 부모클래스에 잡아야해
//여기선 싱글 테이블 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")  //ex. book이면 어떻게 할거야.. 뭔소리야
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // 보통 stock quantity를 가져와서 거기서 지지고 볶고 삶고 마지막에 setQuantity 같은 방식으로 코딩을 했읉텐데
    // BUT, 객체 지향적으로 생각해보면 데이터를 가지고 있는 쪽에 비즈니스 메서드가 있는게 가장 좋음. 응집력 있음
    // quantity를 변경할일 있으면 setter를 쓰는게 아니라 이렇게 핵심 비즈니스 로직으로 변경하도록!!
    /**
     * stock(재고) 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock(재고) 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
