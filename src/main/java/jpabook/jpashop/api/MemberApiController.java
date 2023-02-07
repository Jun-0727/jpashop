package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// @Controller + @ResponseBody = @RestController
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     *   회원 조회 API .v1 : 엔티티를 직접 노출
     *   관련된 모든 정보를 가져옴(회원정보, 주문정보 , ...)
     *   Member 도메인에서 join된 애를 @JsonIgnore 붙여주면 회원정보만 뜨긴하는데
     *   결론 : 이렇게 쓰면안됨
     */
    @GetMapping("/api/v1/members")
    public List<Member> MemberV1() {
        return memberService.findMembers();
    }

    /**
     *   회원 조회 API .v2 : DTO 만들어서 조회
     *
     *
     *   이렇게 써야됨
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());


        return new Result(collect.size(), collect);
        //return new Result(collect);

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }



    /**
     *   회원 등록 API .v1 : 엔티티를 파라미터로 받아서 사용
     *   결론 : 이렇게 쓰면안됨
     */
    @PostMapping("/api/v1/members")

    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 등록 API .v2 : API 스펙을 위한 별도의 DTO 만들어서 사용
     * 무조건 이 방식을 채택해서 사용하는게 좋다
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 이게 바로 DTO
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        public CreateMemberResponse(Long id) {
            this.id = id;
        }

        private Long id;
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,    // url에  {id}부분 값을 넘겨주는 역할
            @RequestBody @Valid UpdateMemberRequest request) {

        // 수정할 떈 변경감지!
        // 커맨드와 쿼리를 분리해서 사용
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


}
