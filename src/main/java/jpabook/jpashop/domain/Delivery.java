package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //JPA 1대1 관게에서는 fk를 어디에 둬도 됨
    //여기선 fk를 order에 둘거임
    //그럼이제 연관관계 주인은 fk랑 가까이있는 order에 있는 delivery로 주면 됨
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    //Enum으로 만들건데 enum타입은 조심해야되는게있음
    // @Enumerated를 넣어야되는데 이넘 타입 넣을때 ordinary랑 string이 있는데 ordinary 가 default. 꼭 string으로 쓰세요
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
