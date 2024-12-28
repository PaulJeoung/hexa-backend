package io.hexaceps.service;

import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // 상품 리스트 조회
    PageResponseDTO<ProductDTO> getProductList(PageRequestDTO pageRequestDTO);

    // 상품 추가
    @Transactional
    Long addNewProduct(ProductDTO productDTO);

    // 상품 조회
    ProductDTO getProductById(Long productId);

    /*
    상품 수정 로직은 직접 DB에서 수정 할 예정 2024.12.29
    - 사유 : 관리자 권한이며, 유저가 수정할 이유가 없기 때문
    // 상품 수정
    Long updateProduct(ProductDTO productDTO);
     */

    /*
    아래 코드는 잠시 보류...

    // 카테고리 별 상품 조회
    List<ProductDTO> getProductsByCategory(String category);

    // 상품 이름으로 조회
    List<ProductDTO> getProductSearchByName(String keyword);

    // 상품 등록
    ProductDTO createProduct(ProductDTO productDTO, List<MultipartFile> images);
     */

}
