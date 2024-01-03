package dev.jeonghun.repository;

import dev.jeonghun.domain.Address;
import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void 멤버_생성_및_조회() {
        Member saveMember = 멤버_저장();
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(NoSuchElementException::new);

        Assertions.assertThat(saveMember.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void 멤버_삭제() {
        Member saveMember = 멤버_저장();
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        memberRepository.delete(findMember);
        em.flush();

        Optional<Member> deleteMember = memberRepository.findByIdAndDeleted(saveMember.getId(), DeleteFlag.N);
        Assertions.assertThat(deleteMember.isEmpty()).isTrue();
    }

    private Member 멤버_저장() {
        Member member = Member.builder()
                .name("홍길동")
                .phoneNumber("01012345678")
                .email("contact@contact.com")
                .address(Address.builder()
                        .address("아파트 1004동 1004호")
                        .zipcode("1002-2")
                        .build())
                .build();

        return memberRepository.save(member);
    }
}