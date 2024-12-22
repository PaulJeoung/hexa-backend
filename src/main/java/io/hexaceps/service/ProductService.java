package io.hexaceps.service;

import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // 상품 리스트 조회
    PageResponseDTO<ProductDTO> getProductList(PageRequestDTO pageRequestDTO);

    // 상품 추가
    Long addNewProduct(ProductDTO productDTO);

    // 상품 조회
    ProductDTO getProductById(Long productId);

    // 상품 수정
    Long updateProduct(ProductDTO productDTO);

    // 상품 삭제
    void deleteProduct(Long productId);

    /*
    아래 코드는 잠시 보류...

    // 상품 조회 (productId)
    ProductDTO getProductById(Long productId);

    // 카테고리 별 상품 조회
    List<ProductDTO> getProductsByCategory(String category);

    // 상품 이름으로 조회
    List<ProductDTO> getProductSearchByName(String keyword);

    // 상품 등록
    ProductDTO createProduct(ProductDTO productDTO, List<MultipartFile> images);

    // 상품 수정
    ProductDTO updateProduct(Long productId, ProductDTO productDTO, List<MultipartFile> images);

    // 상품 삭제 (미사용)
    void deleteProduct(Long productId);
     */

}
