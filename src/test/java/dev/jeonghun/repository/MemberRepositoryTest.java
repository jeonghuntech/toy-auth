package dev.jeonghun.repository;

import dev.jeonghun.domain.Address;
import dev.jeonghun.domain.Contact;
import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.department.Department;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager em;

    @Test
    void 멤버_생성_및_조회() {
        Member saveMember = 멤버_저장();
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void 멤버_삭제() {
        Member saveMember = 멤버_저장();
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();

        memberRepository.delete(findMember);
        em.flush();
        em.clear();

        Optional<Member> deleteMember = memberRepository.findByIdAndDeleted(saveMember.getId(), DeleteFlag.N);

        assertThat(deleteMember.isEmpty()).isTrue();
    }

    @Test
    void 멤버_수정() {
        Member saveMember = 멤버_저장();
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();

        Contact contact = findMember.getContact();
        Contact newContact = Contact.builder()
                .name(contact.getName())
                .phoneNumber("01022223333")
                .email("이메일.변경@naver.com")
                .build();

        Department findDepartment = 부서_저장();
        Department saveDepartment = departmentRepository.findById(findDepartment.getId()).orElseThrow();

        findMember.changeContact(newContact);
        findMember.changeDepartment(findDepartment);

        // TODO 삭제 예정
        em.flush();
        em.clear();

        findMember = memberRepository.findById(saveMember.getId()).orElseThrow();
        findDepartment = departmentRepository.findById(saveDepartment.getId()).orElseThrow();

        assertThat(findMember.getDepartment()).isEqualTo(findDepartment);
        assertThat(findMember.getContact()).isEqualTo(newContact);
    }

    Department 부서_저장() {
        Department department = Department.builder()
                .name("테스트 부서")
                .build();
        return departmentRepository.save(department);
    }

    Member 멤버_저장() {
        Member member = Member.builder()
                .contact(
                        Contact.builder()
                                .name("홍길동")
                                .phoneNumber("01012345678")
                                .email("contact@contact.com")
                                .build())
                .address(
                        Address.builder()
                                .address("아파트 1004동 1004호")
                                .zipcode("1002-2")
                                .build())
                .build();

        return memberRepository.save(member);
    }
}
