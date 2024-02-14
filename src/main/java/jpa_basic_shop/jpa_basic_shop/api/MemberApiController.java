package jpa_basic_shop.jpa_basic_shop.api;

import jakarta.validation.Valid;
import jpa_basic_shop.jpa_basic_shop.domain.Member;
import jpa_basic_shop.jpa_basic_shop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> getAllMembersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result getAllMembersV2() {
        List<MemberDto> collect = memberService.findMembers().stream()  // orders도 이미 프록시로 끌고 왔다.
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMember2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long memberId, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(memberId, request.getName());
        Member findMember = memberService.findOne(memberId);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> { // 기존 json 데이터를 감싸는 새로운 배열 앞단으로 api에 대한 추가 데이터를 넣을 수 있음
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class PresentMemberResponse {
        private Long id;
        private String name;
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

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
