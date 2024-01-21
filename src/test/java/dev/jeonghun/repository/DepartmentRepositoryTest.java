package dev.jeonghun.repository;

import dev.jeonghun.domain.Address;
import dev.jeonghun.domain.Contact;
import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.department.Department;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void 부서_생성_및_조회() {
        Department dept = Department.builder()
                .name(Department.TOP_DEPARTMENT_NAME)
                .parent(최상위_부서_생성_및_저장())
                .build();

        Department saveTopDept = departmentRepository.save(dept);
        Department findDept = departmentRepository.findById(saveTopDept.getId()).orElseThrow();

        assertThat(findDept.getName()).isEqualTo(Department.TOP_DEPARTMENT_NAME);
    }

    @Test
    void 부서의_멤버_목록_조회() {
        Department department = 부서_생성_및_저장("테스트 부서", null);
        Member member1 = 멤버_생성();
        Member member2 = 멤버_생성();

        member1.changeDepartment(department);
        member2.changeDepartment(department);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        Department findDepartment = departmentRepository.findById(department.getId()).orElseThrow();

        assertThat(findDepartment.getMembers()).contains(member1, member2);
    }

    @Test
    void 하위_부서_목록_생성() {
        Department parent = 부서_생성_및_저장("상위 부서", null);
        Department childA = 부서_생성_및_저장("자식 부서 A", parent);
        Department childB = 부서_생성_및_저장("자식 부서 B", parent);

        assertThat(childA.getName()).isEqualTo("자식 부서 A");
        assertThat(childB.getName()).isEqualTo("자식 부서 B");

        assertThat(childA.getParent()).isEqualTo(parent);
        assertThat(childB.getParent()).isEqualTo(parent);


        assertThat(parent.getChilds()).contains(childA);
    }

    @Test
    void 상위_부서_변경() {
        Department parentA = 부서_생성_및_저장("상위 부서 A", null);
        Department parentB = 부서_생성_및_저장("상위 부서 B", null);
        Department child = 부서_생성_및_저장("하위 부서", parentA);

        child.changeParent(parentB);

        assertThat(child.getParent()).isNotEqualTo(parentA);
        assertThat(child.getParent()).isEqualTo(parentB);

        assertThat(parentA.getChilds()).isEmpty();
        assertThat(parentB.getChilds()).containsExactly(child);
    }

    @Test
    void 하위_부서_추가() {
        Department parentA = 부서_생성_및_저장("상위 부서 A", null);
        Department parentB = 부서_생성_및_저장("상위 부서 B", null);
        Department child = 부서_생성_및_저장("하위 부서", parentA);

        parentB.addChild(child);

        assertThat(child.getParent()).isNotEqualTo(parentA);
        assertThat(child.getParent()).isEqualTo(parentB);

        assertThat(parentA.getChilds()).isEmpty();
        assertThat(parentB.getChilds()).containsExactly(child);
    }

    Department 부서_생성_및_저장(String name, Department parent) {
        Department dept = Department.builder()
                .name(name)
                .parent(parent)
                .build();
        return departmentRepository.save(dept);
    }

    Department 최상위_부서_생성_및_저장() {
        Department dept = Department.builder()
                .name(Department.TOP_DEPARTMENT_NAME)
                .build();
        return departmentRepository.save(dept);
    }

    Member 멤버_생성() {
        return Member.builder()
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
    }
}
