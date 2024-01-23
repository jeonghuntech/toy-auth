package dev.jeonghun.repository;

import dev.jeonghun.domain.Board;
import dev.jeonghun.domain.Comment;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager em;

    @Test
    void 게시판_생성_및_조회() {
        Board board = Board.builder().title("제목 1").content("내용 1").build();
        Board savedBoard = boardRepository.save(board);
        Board findBoard = boardRepository.findById(savedBoard.getId()).orElseThrow();

        System.out.println("findBoard = " + findBoard);
        assertThat(savedBoard.getId()).isEqualTo(findBoard.getId());
    }

    @Test
    void 댓글_추가_by_영속성_전이() {
        Board board = Board.builder().title("제목 1").content("내용 1").build();

        Comment comment1 = Comment.builder()
                .content("댓글 내용1")
                .build();

        Comment comment2 = Comment.builder()
                .content("댓글 내용2")
                .build();

        board.addComment(comment1);
        board.addComment(comment2);

        Board savedBoard = boardRepository.save(board);
        Board findBoard = boardRepository.findById(savedBoard.getId()).orElseThrow();

        assertThat(findBoard.getComments()).contains(comment1, comment2);
    }

    @Test
    void 댓글_삭제_by_영속성_전이() {
        Board board = Board.builder().title("제목 1").content("내용 1").build();

        Comment comment1 = Comment.builder()
                .content("댓글 내용1")
                .build();

        Comment comment2 = Comment.builder()
                .content("댓글 내용2")
                .build();

        board.addComment(comment1);
        board.addComment(comment2);

        Board savedBoard = boardRepository.save(board);
        savedBoard.removeComment(comment1);

        em.flush();
        em.clear();

        Board findBoard = boardRepository.findById(savedBoard.getId())
                .orElseThrow();
        assertThat(findBoard.getComments()).hasSize(1);
        assertThat(commentRepository.findById(comment1.getId()).isEmpty()).isTrue();

        boardRepository.delete(savedBoard);
        assertThat(boardRepository.findById(savedBoard.getId()).isEmpty()).isTrue();
        assertThat(commentRepository.findById(comment2.getId()).isEmpty()).isTrue();
    }
}
