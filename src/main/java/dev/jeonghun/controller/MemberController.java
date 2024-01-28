package dev.jeonghun.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeonghun.domain.dto.MemberDto;
import dev.jeonghun.domain.dto.PageDto;
import dev.jeonghun.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/list")
	public ResponseEntity<PageDto<MemberDto>> findAllMembers(Pageable pageable) {
		return ResponseEntity.ok(memberService.findAllMembers(pageable));
	}
}
