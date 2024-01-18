package dev.jeonghun.repository;

import dev.jeonghun.domain.board.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    void 게시판_생성_및_조회() {
        Board board = Board.builder().title("제목 1").content("내용 1").build();
        Board savedBoard = boardRepository.save(board);
        Board findBoard = boardRepository.findById(savedBoard.getId()).orElseThrow();

        System.out.println("findBoard = " + findBoard);
        Assertions.assertThat(savedBoard.getId()).isEqualTo(findBoard.getId());
    }


}

