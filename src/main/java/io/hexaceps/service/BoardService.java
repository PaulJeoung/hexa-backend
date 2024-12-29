package io.hexaceps.service;

import io.hexaceps.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    // 카테고리 별 게시판 조회
    Page<BoardDTO> getBoardsByCategory(String category, Pageable pageable);

    // 검색 결과 조회
    Page<BoardDTO> getSearchBoards(String category, String keyword, Pageable pageable);

    // 게시글 id 조회 및 조회시 조회수 증가
    BoardDTO getBoardCountById(Long id);

    // 게시글 생성 (관리자 only)
    BoardDTO createBoard(BoardDTO boardDTO);

    // 게시글 수정 (관리자 only)
    BoardDTO updateBoard(Long id, BoardDTO boardDTO);
}
