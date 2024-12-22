package io.hexaceps.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class ProductDTO {

    private Long productId;

    private String category;

    private String productName;

    private int price;

    private String productBrand;

    private int productStock;

    private String productDescription;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    private String size;

    // ProductImage 에 넣을 리스트 형태를 DTO에서 생성
    private List<MultipartFile> files = new ArrayList<>();

    // 업로드가 완료된 파일의 이름만 문자열로 저장하는 리스트 (조회용도)
    private List<String> uploadFileNames = new ArrayList<>();

    // ProductSite에 넣을 내용을 리스트 형태로 DTO에서 생성
    private List<String> productSiteNames = new ArrayList<>();

}
