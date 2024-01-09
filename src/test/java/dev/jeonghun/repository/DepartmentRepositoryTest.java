package dev.jeonghun.repository;

import dev.jeonghun.domain.department.Department;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager em;

    @Test
    void 부서_생성() {
        Department dept = Department.builder()
                .name(Department.TOP_DEPARTMENT_NAME)
                .parent(최상위_부서_생성_및_저장())
                .build();

        Department saveTopDept = departmentRepository.save(dept);
        Department findDept = departmentRepository.findById(saveTopDept.getId()).get();

        assertThat(findDept.getName()).isEqualTo(Department.TOP_DEPARTMENT_NAME);
    }

    @Test
    void 하위_부서_목록_생성() {
        Department parent = 부서_생성_및_저장("상위 부서", null);
        Department childA = 부서_생성_및_저장("자식 부서 A", parent);
        Department childB = 부서_생성_및_저장("자식 부서 B", parent);

        childA.changeParent(parent);
        childB.changeParent(parent);

        assertThat(childA.getName()).isEqualTo("자식 부서 A");
        assertThat(childB.getName()).isEqualTo("자식 부서 B");

        assertThat(childA.getParent()).isEqualTo(parent);
        assertThat(childB.getParent()).isEqualTo(parent);

        assertThat(parent.getChilds()).containsExactly(childA, childB);
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
}
