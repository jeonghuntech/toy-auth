package dev.jeonghun.domain.dto;

import dev.jeonghun.domain.Member;

/**
 * DTO for {@link dev.jeonghun.domain.Member}
 */
public record MemberDto(
        Long id,
        int age,
        String name,
        String phoneNumber,
        String email
) {
    public MemberDto(Member member) {
        this(
                member.getId(),
                member.getAge(),
                member.getContact().getName(),
                member.getContact().getPhoneNumber(),
                member.getContact().getEmail()
        );
    }
}
