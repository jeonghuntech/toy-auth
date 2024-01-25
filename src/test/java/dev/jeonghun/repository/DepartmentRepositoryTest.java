package dev.jeonghun.repository;

import dev.jeonghun.domain.Department;
import dev.jeonghun.domain.DepartmentFixture;
import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.MemberFixture;
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
        Department department = DepartmentFixture.ROOT_TEAM.department();

        Department saveTopDept = departmentRepository.save(department);
        Department findDept = departmentRepository.findById(saveTopDept.getId()).orElseThrow();

        assertThat(findDept.getName()).isEqualTo(department.getName());
    }

    @Test
    void 부서의_멤버_목록_조회() {
        Department department = DepartmentFixture.ROOT_TEAM.department();
        departmentRepository.save(department);

        Member member1 = MemberFixture.KIM.member();
        Member member2 = MemberFixture.PARK.member();

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
        Department parent = DepartmentFixture.newDepartment("상위 부서");
        Department childA = DepartmentFixture.newDepartment("자식 부서 A", parent);
        Department childB = DepartmentFixture.newDepartment("자식 부서 B", parent);

        assertThat(childA.getName()).isEqualTo("자식 부서 A");
        assertThat(childB.getName()).isEqualTo("자식 부서 B");

        assertThat(childA.getParent()).isEqualTo(parent);
        assertThat(childB.getParent()).isEqualTo(parent);


        assertThat(parent.getChilds()).contains(childA);
    }

    @Test
    void 상위_부서_변경() {
        Department parentA = DepartmentFixture.newDepartment("상위 부서 A", null);
        Department parentB = DepartmentFixture.newDepartment("상위 부서 B", null);
        Department child = DepartmentFixture.newDepartment("하위 부서", parentA);

        child.changeParent(parentB);

        assertThat(child.getParent()).isNotEqualTo(parentA);
        assertThat(child.getParent()).isEqualTo(parentB);

        assertThat(parentA.getChilds()).isEmpty();
        assertThat(parentB.getChilds()).containsExactly(child);
    }

    @Test
    void 하위_부서_추가() {
        Department parentA = DepartmentFixture.newDepartment("상위 부서 A", null);
        Department parentB = DepartmentFixture.newDepartment("상위 부서 B", null);
        Department child = DepartmentFixture.newDepartment("하위 부서", parentA);

        parentB.addChild(child);

        assertThat(child.getParent()).isNotEqualTo(parentA);
        assertThat(child.getParent()).isEqualTo(parentB);

        assertThat(parentA.getChilds()).isEmpty();
        assertThat(parentB.getChilds()).containsExactly(child);
    }
}
