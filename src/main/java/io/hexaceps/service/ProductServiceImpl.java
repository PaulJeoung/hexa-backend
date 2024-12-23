package io.hexaceps.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    // private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 형태로 변환 하기 위해서 사용

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
        Page<Object[]> result = productRepository.findBySelectImageList(pageable);

        List<ProductDTO> dtoList = result.get().map(list -> {
            HexaProduct product = (HexaProduct) list[0]; // 0번째는 product
            ProductImage productImage = (ProductImage) list[1]; // productImage
            // ProductSite productSite = (ProductSite) list[2]; // productSite 정보
            ProductSite productSite = null;
            if(list.length > 2 && list[2] instanceof ProductSite){
                productSite = (ProductSite) list[2];
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .productDescription(product.getProductDescription())
                    .productBrand(product.getProductBrand())
                    .price(product.getPrice())
                    .productStock(product.getProductStock())
                    .category(product.getCategory())
                    .build();
            // 상품 이미지와 판매 사이트 링크 정보를 DTO에 set
            String imageFileName = productImage.getFileName();
            String siteName = productSite != null ? productSite.getSiteLink() : null; // productSite.getSiteLink();
            productDTO.setUploadFileNames(List.of(imageFileName));
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
    public Long addNewProduct(ProductDTO productDTO) {
        HexaProduct product = mapToEntity(productDTO);
        log.info("Add new product: {}", product);
        log.info("Product Image & Site : {} {}", productDTO.getUploadFileNames(), productDTO.getProductSiteNames());
        Long productId = productRepository.save(product).getProductId();
        return productId;
    }

    // 하나의 상품 조회
    @Override
    public ProductDTO getProductById(Long productId) {
        Optional<HexaProduct> result = productRepository.findById(productId);
        HexaProduct product = result.orElseThrow();
        ProductDTO productDTO = mapToDTO(product);
        return productDTO;
    }

    // 상품 수정
    @Override
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

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // DTO를 Entity로 Map으로 처리 후 반환
    private HexaProduct mapToEntity(ProductDTO productDTO) {
        HexaProduct product = HexaProduct.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDescription(productDTO.getProductDescription())
                .productBrand(productDTO.getProductBrand())
                .price(productDTO.getPrice())
                .productStock(productDTO.getProductStock())
                .category(productDTO.getCategory())
                .size(productDTO.getSize())
                .registeredAt(productDTO.getRegisteredAt())
                .updatedAt(productDTO.getUpdatedAt())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames != null && uploadFileNames.isEmpty()) {
            // return product;
            uploadFileNames.forEach(product::addImageString);
        }
//        uploadFileNames.stream().forEach(uploadFileName -> {
//            product.addImageString(uploadFileName);
//        });

        List<String> productSiteNames = productDTO.getProductSiteNames();

        if(productDTO.getProductSiteNames() != null && productDTO.getProductSiteNames().isEmpty()) {
            productSiteNames.forEach(siteName ->
                    product.addSiteLink(siteName, siteName.length())
            );
            //return product;
        }
//        for (int i = 0; i < productDTO.getProductSiteNames().size(); i++) {
//            ProductSite productSite = ProductSite.builder()
//                    .siteLink(productSiteNames.get(i))
//                    .siteOrd(i)
//                    .build();
//            product.addSiteLink(productSite);
//        }
//        productSiteNames.stream().forEach(productSiteName -> {
//            product.addSiteLink(productSiteName, productSiteName.length());
//        });

        return product;

    }

    // Entity를 DTO로 Map으로 처리 후 반환
    private ProductDTO mapToDTO(HexaProduct product) {
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

        // Image 정보들을 uploadFileNames의 List로 가지고 와서 DTO 출력
        List<ProductImage> imageList = product.getImageList();
        if(imageList != null && imageList.isEmpty()) {
            return productDTO;
        }
        List<String> fileNameList = imageList.stream().map(ProductImage::getFileName).collect(Collectors.toList());
        productDTO.setUploadFileNames(fileNameList);

        // 판매 Site 정보들을 siteNameList의 List로 가지고 와서 DTO 출력
        List<ProductSite> siteList = product.getSiteList();
        if(siteList != null && siteList.isEmpty()) {
            return productDTO;
        }
        List<String> siteNameList = siteList.stream().map(ProductSite::getSiteLink).collect(Collectors.toList());
        productDTO.setProductSiteNames(siteNameList);

        return productDTO;
    }


    /*
    코드 분석 전까지 미사용
    "trace": "org.springframework.web.servlet.resource.NoResourceFoundException: No static resource api/products.\n\tat org.springframework.web.servlet.resource.ResourceHttpRequestHandler.

    private static final String IMAGE_UPLOAD_PATH = "/upload/";
    private static final String DEFAULT_IMAGE = "/upload/default.jpg";

    // 상품 id로 조회
    @Override
    public ProductDTO getProductById(Long productId) {
        HexaProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    // 상품 카테고리로 조회
    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<HexaProduct> products = productRepository.findByCategory(category);
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 상품 이름으로 조회
    @Override
    public List<ProductDTO> getProductSearchByName(String keyword) {
        List<HexaProduct> products = productRepository.findByProductNameLike(keyword);
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 상품 등록 서비스 추가
    @Override
    public ProductDTO createProduct(ProductDTO productDTO, List<MultipartFile> images) {
        List<String> imagePaths = saveImages(images);

        HexaProduct product = HexaProduct.builder()
                // .catetory(productDTO.getCategory())
                .productName(productDTO.getProductName())
                .productBrand(productDTO.getProductBrand())
                .productStock(productDTO.getProductStock())
                .productDescription(productDTO.getProductDescription())
                .registeredAt(productDTO.getRegisteredAt())
                .updatedAt(productDTO.getUpdatedAt())
                .size(productDTO.getSize())
                .price(productDTO.getPrice())
                .build();

        HexaProduct savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    // 상품 수정 서비스 추가
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO, List<MultipartFile> images) {
        HexaProduct existProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<String> imagePaths = saveImages(images);

        ProductDTO updatedProduct = mapToDTO(existProduct);
        updatedProduct.setProductName(productDTO.getProductName());
        updatedProduct.setProductBrand(productDTO.getProductBrand());
        updatedProduct.setProductStock(productDTO.getProductStock());
        updatedProduct.setProductDescription(productDTO.getProductDescription());
        updatedProduct.setRegisteredAt(productDTO.getRegisteredAt());
        updatedProduct.setUpdatedAt(productDTO.getUpdatedAt());
        updatedProduct.setSize(productDTO.getSize());
        updatedProduct.setPrice(productDTO.getPrice());
        HexaProduct savedProduct = productRepository.save(existProduct);

        return mapToDTO(savedProduct);
    }

    // 상품 삭제 서비스 추가 (실제는 미사용)
    @Override
    public void deleteProduct(Long productId) {
        HexaProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    // 상품 조회를 위해서 DTO 를 빌드 패턴으로 반환
    private ProductDTO mapToDTO(HexaProduct product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .category(product.getCategory())
                .productName(product.getProductName())
                .productBrand(product.getProductBrand())
                .productStock(product.getProductStock())
                .productDescription(product.getProductDescription())
                .registeredAt(product.getRegisteredAt())
                .size(product.getSize())
                .updatedAt(product.getUpdatedAt())
                .price(product.getPrice())
                .build();
    }

    // 이미지 저장 및 Path 저장
    private List<String> saveImages(List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();

        if (images != null || images.isEmpty()) {
            imagePaths.add(DEFAULT_IMAGE); // 이미지가 없는 경우 기본 이미지를 대체
            return imagePaths;
        }
        if (images.size() > 4) {
            throw new RuntimeException("사진은 최대 4장까지 업로드 할수 있습니다");
        }

        for (MultipartFile image : images) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(IMAGE_UPLOAD_PATH + File.separator + fileName);
            try {
                image.transferTo(file);
                imagePaths.add("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image " + fileName, e);
            }
        }
        return imagePaths;
    }

    // 이미지 경로 저장을 위해서 JSON 형태로 파싱
    private String toJson(List<String> images) {
        try {
            return objectMapper.writeValueAsString(images);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert images to JSON", e);
        }
    }

    // 이미지 경로를 읽기 위한 전처리 프로세스
    private List<String> fromJson(String images) {
        try {
            return objectMapper.readValue(images, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert images to JSON", e);
        }
    }

     */
}
