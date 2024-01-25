package dev.jeonghun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeonghun.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
