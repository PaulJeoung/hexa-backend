package io.hexaceps.service;

import io.hexaceps.dao.HexaProduct;
import io.hexaceps.dao.ProductImage;
import io.hexaceps.dao.ProductSite;
import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;
import io.hexaceps.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // 상품 리스트 가져오기
    @Override
    public PageResponseDTO<ProductDTO> getProductList(PageRequestDTO pageRequestDTO) {
        // 페이지 목록
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("productId").descending()
        );

        // 레포지터리에서 목록 가져오기 (대표이미지 Get)
        Page<Object[]> result = productRepository.findBySelectImageAndSiteList(pageable);

        List<ProductDTO> dtoList = result.get().map(list -> {
            HexaProduct product = (HexaProduct) list[0]; // 0번째는 product
            ProductImage productImage = (ProductImage) list[1]; // productImage
            ProductSite productSite = (ProductSite) list[2]; // productSite 정보

            ProductDTO productDTO = ProductDTO.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .productDescription(product.getProductDescription())
                    .productBrand(product.getProductBrand())
                    .price(product.getPrice())
                    .size(product.getSize())
                    .productStock(product.getProductStock())
                    .category(product.getCategory())
                    .registeredAt(product.getRegisteredAt())
                    .updatedAt(product.getUpdatedAt())
                    .build();
            // 상품 이미지와 판매 사이트 링크 정보를 DTO에 set
            String imageFileName = productImage != null ? productImage.getFileName() : null;
            String siteName = productSite != null ? productSite.getSiteLink() : null; // productSite.getSiteLink();
            productDTO.setUploadFileNames(imageFileName != null ? List.of(imageFileName) : List.of());
            productDTO.setProductSiteNames(siteName != null ? List.of(siteName) : List.of()); // (List.of(siteName));
            return productDTO;
        }).toList();
        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .build();
    }

    // 상품 추가
    @Override
    @Transactional
    public Long addNewProduct(ProductDTO productDTO) {
        HexaProduct product = dtoToEntity(productDTO);
        product.setRegisteredAt(LocalDate.now()); // 현재 날짜 저장 추가
        log.info("서비스로직에서의 product(저장전) {}", product);
        Long productId = productRepository.save(product).getProductId();
        return productId;
    }

    // addNewProduct() 상품 저장시에 DTO 정보를 Entity 로 변환
    private HexaProduct dtoToEntity(ProductDTO productDTO) {
        HexaProduct product = HexaProduct.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDescription(productDTO.getProductDescription())
                .productBrand(productDTO.getProductBrand())
                .price(productDTO.getPrice())
                .productStock(productDTO.getProductStock())
                .category(productDTO.getCategory())
                .size(productDTO.getSize())
                // .registeredAt(LocalDate.now())
                // .updatedAt(LocalDate.now()) 수정할떄, set으로 처리
                .build();
        // 이미지 리스트 추가
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            for (String fileName : uploadFileNames) {
                product.addImageString(fileName);
            }
        }
        // 사이트 링크 리스트 추가
        List<String> productSiteNames = productDTO.getProductSiteNames();
        if (productSiteNames != null && !productSiteNames.isEmpty()) {
            for (int i = 0; i < productSiteNames.size(); i++) {
                product.addSiteLink(productSiteNames.get(i), i);
            }
        }
        return product;
    }

    // 하나의 상품 조회
    @Override
    public ProductDTO getProductById(Long productId) {
        Optional<HexaProduct> result = productRepository.findById(productId);
        HexaProduct product = result.orElseThrow();
        ProductDTO productDTO = entityToDTO(product);
        return productDTO;
    }

    // getProductById() 상품을 productId로 조회 시 Entity 정보를 DTO 로 변환
    private ProductDTO entityToDTO(HexaProduct product) {
        ProductDTO productDTO = ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productBrand(product.getProductBrand())
                .price(product.getPrice())
                .productStock(product.getProductStock())
                .category(product.getCategory())
                .size(product.getSize())
                .registeredAt(product.getRegisteredAt())
                .updatedAt(product.getUpdatedAt())
                .build();
        // Image 정보들을 uploadFileNames의 List로 가지고 와서 DTO에 SET
        List<ProductImage> productImageList = product.getImageList();
        if (productImageList != null) {
            List<String> imageFileNames = productImageList.stream()
                    .map(ProductImage::getFileName)
                    .collect(Collectors.toList());
            productDTO.setUploadFileNames(imageFileNames);
        }
        // 판매 Site 정보들을 siteNameList의 List로 가지고 와서 DTO에 SET
        List<ProductSite> siteList = product.getSiteList();
        if (siteList != null) {
            List<String> siteNameList = siteList.stream()
                    .map(ProductSite::getSiteLink)
                    .collect(Collectors.toList());
            productDTO.setProductSiteNames(siteNameList);
        }
        return productDTO;
    }

    /*
    상품 수정 로직은 직접 DB에서 수정 할 예정 2024.12.29
    - 사유 : 관리자 권한이며, 유저가 수정할 이유가 없기 때문

    @Override // 상품 수정
    public Long updateProduct(ProductDTO productDTO) {
        Optional<HexaProduct> result = productRepository.findById(productDTO.getProductId());
        HexaProduct product = result.orElseThrow();
        // DAO 에서 setter로 선언한 값들 모두 set, 수정
        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setProductBrand(productDTO.getProductBrand());
        product.setPrice(productDTO.getPrice());
        product.setProductStock(productDTO.getProductStock());
        product.setCategory(productDTO.getCategory());
        product.setUpdatedAt(productDTO.getUpdatedAt());
        product.setSize(productDTO.getSize());
        product.setProductStock(productDTO.getProductStock());
        // 우선 리스트 정보를 모두 삭제 한 뒤, 입력한 이미지 정보를 가지고 온 뒤 저장
        product.clearImageList();
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.stream().forEach(uploadFileName -> {
                product.addImageString(uploadFileName);
            });
        }
        product.clearSiteList();
        List<String> siteNames = productDTO.getProductSiteNames();
        if (siteNames != null && !siteNames.isEmpty()) {
            siteNames.stream().forEach(siteName -> {
                product.addSiteLink(siteName, siteName.length());;
            });
        }
        Long productId = productRepository.save(product).getProductId();
        return productId;
    }
     */
}
