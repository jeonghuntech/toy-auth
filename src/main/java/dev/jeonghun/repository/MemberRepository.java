package dev.jeonghun.repository;

import dev.jeonghun.domain.DeleteFlag;
import dev.jeonghun.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeleted(long userId, DeleteFlag deleted);
}
