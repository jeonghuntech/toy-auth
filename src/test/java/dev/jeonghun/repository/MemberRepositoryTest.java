package dev.jeonghun.repository;

import dev.jeonghun.common.MemberFixture;
import dev.jeonghun.config.P6SpyFormatter;
import dev.jeonghun.domain.Contact;
import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Department;
import dev.jeonghun.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(P6SpyFormatter.class)
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
    void 나이가_5보다_크고_특정_이름을_포함하는_멤버를_조회한다() {

        멤버_목록_생성_및_저장("홍길동", 10);
        멤버_목록_생성_및_저장("홍길서", 20);

        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "contact.name"));
        Page<Member> findMembers = memberRepository.findByContactNameContainsAndAgeGreaterThan("홍길", 5, pageRequest);

        List<Member> content = findMembers.getContent();

        assertThat(content).hasSize(10);
        assertThat(findMembers.getTotalElements()).isEqualTo(20);
        assertThat(findMembers.getNumber()).isEqualTo(1);
        assertThat(findMembers.getTotalPages()).isEqualTo(2);
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

    @Test
    void 특정나이의_멤버의_나이를_1씩_증가시킨다() {
        멤버_목록_생성_및_저장("홍길동", 10);

        // 벌크 연산은 영속성 반영이 안된다. 영속성 컨텍스트를 비우고 하거나 사용후에는 초기화 하자
//        em.flush();
//        em.clear();
        int count = memberRepository.bulkAgePlus(5);
        em.flush();
        em.clear();

        assertThat(count).isEqualTo(5);
    }

    @Test
    void 나이를_기준으로_멤버를_조회한다() {
        멤버_목록_생성_및_저장("홍길동", 10);

        List<Member> findMembers = memberRepository.findByAgeGreaterThan(5);

        assertThat(findMembers).hasSize(5);
    }

    Member 멤버_생성_및_저장(String name) {
        return memberRepository.save(MemberFixture.newMember(0, name));
    }

    void 멤버_목록_생성_및_저장(String name, int count) {
        memberRepository.saveAll(MemberFixture.newMemberList(name, count));
    }

    Department 부서_생성_및_저장(String name) {
        Department dept = Department.builder()
                .name(name)
                .parent(null)
                .build();
        return departmentRepository.save(dept);
    }
}
