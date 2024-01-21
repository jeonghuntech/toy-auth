package dev.jeonghun.repository;

import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeleted(long userId, DeleteFlag deleted);

    @Query("""
            select m
            from Member m
            join fetch m.department d
            where m.contact.name = :userName and d.name = :departmentName
            """)
    List<Member> findMember(@Param("departmentName") String departmentName, @Param("userName") String userName);

    @Query(value = """
                select m
                from Member m
                left join fetch m.department d
                where m.age > :age and m.contact.name like %:name%
            """,
            countQuery = """
                        select count(m)
                        from Member m
                        where m.age > :age
                            and m.contact.name like %:name%
                    """
    )
    Page<Member> findByContactNameContainsAndAgeGreaterThan(String name, int age, Pageable pageable);
    //    Page<Member> findByContactNameContainsAndAgeGreaterThan(String name, int age, Pageable pageable);
}
