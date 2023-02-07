package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 엔티티를 직접 노출
     * 엔티티를 파라미터로 받아서 그대로 사용하는 방법
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString((new OrderSearch()));
        return all;
    }


    /**
     * DTO
     * 엔티티가 외부로 노출되는 것을 방지하기위해 DTO로 엔티티를 한번 감싸는 방법
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // 현재 ORDER가 2개잖아
        // N + 1 문제 --> 1 + N 문제 : 첫번쩨 쿼리의 결과로 N번 만큼 쿼리가 추가 실행되는 경우
        // 1은 맨 처음 orders 가져오는 쿼리.
        // 첫번째 N은 회원, 두번째 N은 배송
        // 1 + N(2) + N(2) = 5 번 실행됐다.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }


    /**
     * DTO + fetch join
     * 엔티티를 한번 감싸는 것은 같고
     * 추가로 N + 1문제까지 해결
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }


    /**
     * 최적화 중 최적화
     * 복잡하고 귀찮다
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        // DTO가 엔티티를 파라미터로 받는건 괜찮다
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();         // 여기서 LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // 여기서 LAZY 초기화
        }
    }







}
