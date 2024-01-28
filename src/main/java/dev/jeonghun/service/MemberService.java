package dev.jeonghun.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.dto.MemberDto;
import dev.jeonghun.domain.dto.PageDto;
import dev.jeonghun.repository.MemberRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public PageDto<MemberDto> findAllMembers(Pageable pageable) {
		Page<MemberDto> findMembers = memberRepository.findAllBy(pageable)
			.map(MemberDto::new);
		return new PageDto<>(findMembers);
	}

	public void save(Member member) {
		memberRepository.save(member);
	}

	public void saveAll(List<Member> members) {
		memberRepository.saveAll(members);
	}
}
