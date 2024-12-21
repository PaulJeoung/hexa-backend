package io.hexaceps.service;

import io.hexaceps.dao.HexaBoard;
import io.hexaceps.dto.BoardDTO;
import io.hexaceps.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 카테고리 별로 조회 하는 서비스 추가
    @Override
    public Page<BoardDTO> getBoardsByCategory(String category, Pageable pageable) {
        Page<HexaBoard> boards = boardRepository.findByCategory(category, pageable);
        log.info("getBoardsByCategory : {}", boards.toString());
        return boards.map(this::mapToDTO);
    }

    // 키워드 별로 조회 하는 서비스 추가 (카테고리 값을 함께 받아서 필터 처럼 적용)
    @Override
    public Page<BoardDTO> getSearchBoards(String category, String keyword, Pageable pageable) {
        Page<HexaBoard> boards = boardRepository.searchByCategoryAndKeyword(category, keyword, pageable);
        return boards.map(this::mapToDTO);
    }

    // 조회 하는 경우에 view count 증가 하는 서비스 구현
    @Override
    @Transactional
    public BoardDTO getBoardCountById(Long id) {
        boardRepository.incrementCount(id);
        HexaBoard board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found : "+id));
        return mapToDTO(board);
    }

    // 게시글 생성 서비스 추가
    @Override
    @Transactional
    public BoardDTO createBoard(BoardDTO boardDTO) {
        HexaBoard board = HexaBoard.builder()
                .category(boardDTO.getCategory())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .author(boardDTO.getAuthor())
                .createdAt(boardDTO.getCreatedAt())
                // .updatedAt(boardDTO.getUpdatedAt())
                .count(0) // 조회수 init
                .build();

        HexaBoard savedBoard = boardRepository.save(board);

        return mapToDTO(savedBoard);
    }

    // 게시글 업데이트 서비스 추가
    @Override
    @Transactional
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        HexaBoard existBoard = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found : "+ id));
        existBoard.setTitle(boardDTO.getTitle());
        existBoard.setContent(boardDTO.getContent());
        existBoard.setUpdatedAt(boardDTO.getUpdatedAt());
        existBoard.setAuthor(boardDTO.getAuthor());
        existBoard.setUpdatedAt(boardDTO.getUpdatedAt());
        existBoard.setVisible(boardDTO.isVisible());

        HexaBoard updatedBoard = boardRepository.save(existBoard);

        return mapToDTO(updatedBoard);
    }

    // Entity 정보를  DTO로 변환 해서 리턴 (조회를 위해서)
    private BoardDTO mapToDTO(HexaBoard board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setCategory(board.getCategory());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setAuthor(board.getAuthor());
        boardDTO.setCreatedAt(board.getCreatedAt());
        boardDTO.setUpdatedAt(board.getUpdatedAt());
        boardDTO.setVisible(board.isVisible());
        boardDTO.setCount(board.getCount());

        return boardDTO;
    }
}
