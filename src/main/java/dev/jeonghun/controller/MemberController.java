package dev.jeonghun.controller;

import dev.jeonghun.domain.dto.MemberDto;
import dev.jeonghun.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/members")
    public ResponseEntity<Page<MemberDto>> findAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAllMembers(pageable));
    }
}
