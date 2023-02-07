package jpabook.jpashop.domain;import lombok.AccessLevel;import lombok.Getter;import lombok.NoArgsConstructor;import lombok.Setter;import org.hibernate.annotations.BatchSize;import org.hibernate.boot.model.source.spi.FetchCharacteristics;import javax.persistence.*;import java.time.LocalDateTime;import java.util.ArrayList;import java.util.List;@Entity@Table(name = "orders")@Getter @Setter@NoArgsConstructor(access = AccessLevel.PROTECTED)public class Order {    @Id @GeneratedValue    @Column(name = "order_id")  // column을 테이블명의 아이디. DB들이 이 방식을 선호    private Long id;    // ___ToOne 인 애들은 기본 fetch가 EAGER    // 그래서 LAZY로 바꿔줘야함    // Member orders와 양방향 연관관계    // 양방향 연관관계는 관계의 주인을 정해줘야헤. Order의 회원을 바꿀떄 여기의 값을 바꿀 수 있고 반대로 Member에서 orderList의 값을 바꿀수도있어    // 양방향 참조인데 fk를 가지고 있는건 orders!!    // 그래서 누가 주인이라고? fk가 가까운애?    // Order에 있는 member를 주인으로 잡아야한다는데    // 주인이라는게  Member 개체 vs Order개체에 있는 Member 를 비교하는거였어?    @ManyToOne(fetch = FetchType.LAZY)    @JoinColumn(name = "member_id")  //추가로 joinColumn. 매핑을 뭘로 할거냐..?    private Member member;    //___ToMany 인 애들은 기본 fetch가 LAZY    @BatchSize(size = 1000)    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)    private List<OrderItem> orderItems = new ArrayList<>();    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)    @JoinColumn(name = "delivery_id")    private Delivery delivery;    private LocalDateTime orderDate; // 주문시간    @Enumerated(EnumType.STRING)    private OrderStatus status; // 주문상태    //==연관관계 메서드=//    public void setMember(Member member) {        this.member = member;        member.getOrders().add(this);    }    public void addOrderItem(OrderItem orderItem) {        orderItems.add(orderItem);        orderItem.setOrder(this);    }    public void setDelivery(Delivery delivery) {        this.delivery = delivery;        delivery.setOrder(this);    }    //==생성 메서드==//    // 각각을 set, set, set.. 하는게 아니라 생성 메서드로 한번에!!    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {        Order order = new Order();        order.setMember(member);        order.setDelivery(delivery);        for (OrderItem orderItem : orderItems) {            order.addOrderItem(orderItem);        }        order.setStatus(OrderStatus.ORDER);        order.setOrderDate(LocalDateTime.now());        return order;    }    //==비즈니스 로직==//    // 이것도 domain차원에서 비즈니스 로직 구현    /**     * 주문 취소     */    public void cancel() {        if (delivery.getStatus() == DeliveryStatus.COMP) {            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");        }        this.setStatus(OrderStatus.CANCEL);        for (OrderItem orderItem : orderItems) {        // this를 쓰냐 안쓰냐는 알아서..            orderItem.cancel();        }    }    //==조회 로직==//    /**     * 전체 주문 가격 조회     */    public int getTotalPrice() {        int totalPrice = 0;        for (OrderItem orderItem : orderItems) {            totalPrice += orderItem.getTotalPrice();        }        return totalPrice;    }}