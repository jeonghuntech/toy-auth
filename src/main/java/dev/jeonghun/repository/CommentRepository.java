package dev.jeonghun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeonghun.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
