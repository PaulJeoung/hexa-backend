package io.hexaceps.repository;

import io.hexaceps.dao.HexaBoard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class BoardRepositoryTest {

    @Autowired private BoardRepository boardRepository;

    @Test
    void 게시글생성() {
        for (int i = 1; i < 41; i++) {
            String category = (i%2==0) ? "notice" : "faq";
            HexaBoard board = HexaBoard.builder()
                    .category(category)
                    .title("Board Title - " + i + " This is Test Title" + (i*1001))
                    .content("New content compose, 새로운 내용을 작성 중입니다. orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                    .author("Tester"+i)
                    .createdAt(LocalDateTime.now())
                    .count(0)
                    .build();
            boardRepository.save(board);
        }
    }

}