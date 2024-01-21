package dev.jeonghun.repository;

import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
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
            join m.department d
            where m.contact.name = :userName and d.name = :departmentName
            """)
    List<Member> findMember(String departmentName, @Param("userName") String userName);
}
