package io.hexaceps.repository;

import io.hexaceps.dao.HexaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<HexaBoard, Long> {

    // 카테고리 별 게시판 검색
    Page<HexaBoard> findByCategory(String category, Pageable pageable);

    // 제목 또는 내용 검색 (category param 추가 해서 뷰단에서 고정으로 구현 해야됨)
    @Query("select b from HexaBoard b where b.category = :category and (b.title like  %:keyword% or b.content like %:keyword%)")
    Page<HexaBoard> searchByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword, Pageable pageable);

    // 조회수 증가
    @Modifying
    @Query("update HexaBoard b set b.count = b.count + 1 where b.id = :id")
    void incrementCount(@Param("id") Long id);

}
