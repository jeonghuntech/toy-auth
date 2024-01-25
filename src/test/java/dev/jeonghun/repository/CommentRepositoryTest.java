package dev.jeonghun.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import dev.jeonghun.domain.Comment;

@DataJpaTest
@Transactional
class CommentRepositoryTest {

	@Autowired
	CommentRepository commentRepository;

	@Test
	void 댓글_생성_및_조회() {
		Comment comment = Comment.builder()
			.content("댓글 내용1")
			.build();

		Comment savedComment = commentRepository.save(comment);
		Comment findComment = commentRepository.findById(comment.getId()).orElseThrow();

		Assertions.assertThat(savedComment.getId()).isEqualTo(findComment.getId());
	}
}
