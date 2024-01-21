package dev.jeonghun.repository;

import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeleted(long userId, DeleteFlag deleted);
}
