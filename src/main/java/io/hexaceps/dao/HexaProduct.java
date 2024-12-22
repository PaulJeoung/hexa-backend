package io.hexaceps.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@ToString (exclude = "imageList")
@Builder
@AllArgsConstructor @NoArgsConstructor
public class HexaProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private int price;

    private String productBrand;

    private String category;

    private int productStock;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    private String size;

    @ElementCollection
    @Builder.Default // ProductImage 테이블 생성
    private List<ProductImage> imageList = new ArrayList<>();

    @ElementCollection
    @Builder.Default // ProductSite 테이블 생성
    private List<ProductSite> siteList = new ArrayList<>();

    /*
        1. addImage() : 상품의 이미지를 리스트 형태로 저장
        - 이미지 리스트에 갯수에 따라 추가 되는 이미지의 순번을 지정
        - 만일 리스트에 2개의 이미지가 있으면 size(2)를 반환
        2. addImageString() : 파일이름을 기준으로 상품에 이미지를 addImage()에 추가
        3. 파일이름 삭제 (상품 이미지 name)
     */
    public void addImage(ProductImage image) {
        image.setOrd(this.imageList.size());
        this.imageList.add(image);
    }
    public void addImageString(String fileName) {
        ProductImage image = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(image);
    }
    public void clearImageList() {
        this.imageList.clear();
    }

    /*
        1. addImportSite() : 중계 사이트 이름, URL을 리스트에 추가
        2. addImportLink() : 각 중계 사이트의 상품 링크를 addImportSite()에 추가
        3. 내용 삭제 (importList 클리어)
     */
    public void addProductSite(ProductSite productSite) {
        productSite.setSiteOrd(this.siteList.size());
        this.siteList.add(productSite);
    }
    public void addSiteLink(String siteLink) {
        ProductSite productSite = ProductSite.builder()
                .siteLink(siteLink)
                .build();
        addProductSite(productSite);
    }
    public void clearSiteList() {
        this.siteList.clear();
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
