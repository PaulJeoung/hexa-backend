package io.hexaceps.repository;

import io.hexaceps.dao.HexaProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<HexaProduct, Long> {

    // 카테고리로 상품 검색
    List<HexaProduct> findByCategory(String category);

    // 상품 이름으로 검색
    List<HexaProduct> findByProductNameLike(String keyword);

    // 상품 조회
    // @EntityGraph(attributePaths = "imageList", "siteList") // FK 테이블 쿼리 조회 X
    // @Query("select p from HexaProduct p where p.productId = :productId")
    @Query("select p from HexaProduct p join fetch p.imageList join fetch p.siteList where p.productId = :productId")
    Optional<HexaProduct> findByProductId(@Param("productId") Long productId);

    // 상품 삭제

    // /api/product/list 조회시에 사용할 상품 조회 쿼리
    @Query("select p, pi, sl from HexaProduct p" +
            " left join p.imageList pi on pi.ord = 0 " +
            " left join p.siteList sl on sl.siteOrd = 0")
    Page<Object[]> findBySelectImageAndSiteList (Pageable pageable);

    // 상품 조회시 이미지도 함께 출력
    // @Query("select p, pi from HexaProduct p left join p.imageList pi where pi.ord = 0")
    // Page<Object[]> findBySelectImageList(Pageable pageable);

    // 상품 조회시 Site Link도 함께 출력
    // @Query("select p, sl from HexaProduct p left join p.siteList sl where sl.siteOrd = 0")
    // Page<Object[]> findBySelectSiteList(Pageable pageable);

}
