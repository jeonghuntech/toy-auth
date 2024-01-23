package dev.jeonghun.service;

import dev.jeonghun.domain.dto.MemberDto;
import dev.jeonghun.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Page<MemberDto> findAllMembers(Pageable pageable) {
        return memberRepository.findAllBy(pageable)
                .map(MemberDto::new);
    }
}
