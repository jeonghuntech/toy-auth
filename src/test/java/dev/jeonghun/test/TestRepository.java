package dev.jeonghun.test;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.jeonghun.domain.Department;
import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.MemberFixture;
import dev.jeonghun.repository.DepartmentRepository;
import dev.jeonghun.repository.MemberRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
public class TestRepository {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager em;

	@Test
	void 부서의_멤버_목록_조회() {
		Department department = 부서_생성_및_저장("테스트 부서");
		Member member1 = MemberFixture.KIM.member();
		Member member2 = MemberFixture.LEE.member();

		//        member1.changeDepartment(department);
		//        member2.changeDepartment(department);

		Collection<Member> members = department.getMembers();
		members.add(member1);
		members.add(member2);

		memberRepository.save(member1);
		memberRepository.save(member2);

		department.getMembers().forEach(System.out::println);
		System.out.println("department = " + department.getMembers());
		System.out.println("member1 = " + member1);
		System.out.println("member2 = " + member2);

		assertThat(department.getMembers().contains(member1)).isTrue();
		assertThat(department.getMembers().contains(member2)).isTrue();
		assertThat(department.getMembers()).contains(member1, member2);
	}

	@Test
	void 부서의_멤버_목록_ad조회() {
		Member member1 = MemberFixture.KIM.member();
		Member member2 = MemberFixture.LEE.member();

		Collection<Member> set = new HashSet<>();
		set.add(member1);
		set.add(member2);

		memberRepository.save(member1);
		memberRepository.save(member2);

		set.forEach(System.out::println);
		System.out.println("member1 = " + member1);
		System.out.println("member2 = " + member2);

		assertThat(set.contains(member1)).isTrue();
		assertThat(set.contains(member2)).isTrue();
		assertThat(set).contains(member1, member2);
	}

	Department 부서_생성_및_저장(String name) {
		Department dept = Department.builder()
			.name(name)
			.parent(null)
			.build();
		return departmentRepository.save(dept);
	}

	@Test
	void default_batch_fetch_size_다대일() {
		Department department1 = 부서_생성_및_저장("부서1");
		Department department2 = 부서_생성_및_저장("부서2");

		Member member1 = memberRepository.save(MemberFixture.KIM.member());
		Member member2 = memberRepository.save(MemberFixture.LEE.member());
		Member member3 = memberRepository.save(MemberFixture.PARK.member());

		member1.changeDepartment(department1);
		member2.changeDepartment(department2);
		member3.changeDepartment(department2);

		em.flush();
		em.clear();

		String query = "select m from Member m join fetch m.department";

		List<Member> resultList = em.createQuery(query, Member.class).getResultList();

		for (Member member : resultList) {
			System.out.println(
				"################ member = " + member.getContact().getName() + "/" + member.getDepartment());
		}
	}

	@Test
	void default_batch_fetch_size_일대다() {
		Department department1 = 부서_생성_및_저장("부서1");
		Department department2 = 부서_생성_및_저장("부서2");

		Member member1 = memberRepository.save(MemberFixture.KIM.member());
		Member member2 = memberRepository.save(MemberFixture.LEE.member());
		Member member3 = memberRepository.save(MemberFixture.PARK.member());

		member1.changeDepartment(department1);
		member2.changeDepartment(department2);
		member3.changeDepartment(department2);

		em.flush();
		em.clear();

		String query = "select d from Department d";

		List<Department> resultList = em.createQuery(query, Department.class).getResultList();

		for (Department department : resultList) {
			System.out.println(
				"############### department = " + department.getName() + "/" + department.getMembers().size());
			for (Member member : department.getMembers()) {
				System.out.println("member = " + member);
			}
		}
		System.out.println("resultList.size() = " + resultList.size());
	}
}
