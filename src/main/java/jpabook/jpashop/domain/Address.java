package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // 내장타입..?
@Getter
// 값 타입은 변경 불가능하게 설계해야 함
// 그래서 @Setter를 제거하고 생성자에서 값을 초기화해서 변경 불가능하게 만들자!
// JPA에서 @Entity나 @Embeddable은 기본 생성자를 public 또는 protected 로 설정해야 한다.
// public 으로 두는 것 보다는 protected 로 설정하는 것이 더 안전 하다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
