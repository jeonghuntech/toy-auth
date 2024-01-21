package dev.jeonghun.repository;

import dev.jeonghun.domain.Address;
import dev.jeonghun.domain.Contact;
import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.department.Department;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager em;

    @Test
    void 멤버를_생성하고_조회한다() {
        Member saveMember = 멤버_생성_및_저장("홍길동");
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void 부서명과_이름으로_멤버를_조회한다() {
        Department department = 부서_생성_및_저장("개발부");
        Member member1 = 멤버_생성_및_저장("홍길동");
        Member member2 = 멤버_생성_및_저장("강감찬");

        member1.changeDepartment(department);
        member2.changeDepartment(department);

        List<Member> findMembers = memberRepository.findMember("개발부", "홍길동");
        assertThat(findMembers).containsExactly(member1);
    }

    @Test
    void 멤버를_삭제한다() {
        Member saveMember = 멤버_생성_및_저장("홍길동");
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();

        memberRepository.delete(findMember);
        em.flush();
        em.clear();

        Optional<Member> deleteMember = memberRepository.findByIdAndDeleted(saveMember.getId(), DeleteFlag.N);

        assertThat(deleteMember.isEmpty()).isTrue();
    }

    @Test
    void 멤버를_수정한다() {
        Member saveMember = 멤버_생성_및_저장("홍길동");
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();

        Contact contact = findMember.getContact();
        Contact newContact = Contact.builder()
                .name(contact.getName())
                .phoneNumber("01022223333")
                .email("이메일.변경@naver.com")
                .build();

        Department department = 부서_생성_및_저장("부서1");

        findMember.changeContact(newContact);
        findMember.changeDepartment(department);

        // TODO 삭제 예정
        em.flush();
        em.clear();

        findMember = memberRepository.findById(saveMember.getId()).orElseThrow();
        department = departmentRepository.findById(department.getId()).orElseThrow();

        assertThat(findMember.getDepartment()).isEqualTo(department);
        assertThat(findMember.getContact()).isEqualTo(newContact);
    }

    Member 멤버_생성_및_저장(String name) {
        Member member = Member.builder()
                .contact(
                        Contact.builder()
                                .name(name)
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

    Department 부서_생성_및_저장(String name) {
        Department dept = Department.builder()
                .name(name)
                .parent(null)
                .build();
        return departmentRepository.save(dept);
    }
}
