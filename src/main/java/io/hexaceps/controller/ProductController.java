package io.hexaceps.controller;

import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;
import io.hexaceps.service.ProductService;
import io.hexaceps.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CustomFileUtil customFileUtil;

    // 상품 정보 신규 저장
    @PostMapping("/")
    public Map<String, Long> addNewProduct(ProductDTO productDTO) {
        log.info("Add New Product Controller : {}", productDTO);
        // 이미지 파일 먼저 upload 폴더에 저장
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = customFileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames); // 저장 된 정보를 파일 네임으로 ProductDTO에 전달
        Long productId = productService.addNewProduct(productDTO); // 서비스호출

        try { // 로딩모달을 보기위한 쓰레드
            Thread.sleep(2000); // 2초 설정
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Map.of("result", productId);
    }

    // 업로드 된 이미지 파일 조회 (내부 확인 용도 및 추후 react에서 이미지 불러올때)
    // http://localhost:8080/api/products/view/s_15b1f209-5a96-4b13-a04d-967867c8da88_dress0.PNG
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> getUploadFileImage(@PathVariable("fileName") String fileName) {
        return customFileUtil.getFile(fileName);
    }

    // 상품목록 조회
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN')")  // 임시로 권한 설정  'ROLE_USER', 'ROLE_ADMIN'
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> getProductAll(PageRequestDTO pageRequestDTO) {
        return productService.getProductList(pageRequestDTO);
    }

    // 상품 productId로 조회
    @GetMapping// ("/{productId}")
    public ProductDTO getProductById(@RequestParam Long productId){ // @PathVariable("productId") Long pno
        return productService.getProductById(productId);
    }

    //상품수정
    @PutMapping("/{productId}")
    public Map<String, String> updateProduct(@PathVariable("productId") Long productId, ProductDTO productDTO) {
        productDTO.setProductId(productId);

        ProductDTO oldProductDTO = productService.getProductById(productId);
        //기존파일이름
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        //새로 업로드 해야하는 파일
        List<MultipartFile> files = productDTO.getFiles();

        //새로 업로드해야하는 파일의 이름
        List<String> currentUploadFileNames = customFileUtil.saveFiles(files);

        //유지되는 파일
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        //유지되는파일 + 새로 업로드된 파일 => 파일목록을 만든다
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        //수정한다.
        //A,B,C 파일이 있었음, C는 삭제했음, D는 새로 들어왔음 => A,B,D의 처리가 끝남
        productService.updateProduct(productDTO);

        //c를 삭제하여야 한다.
        if(oldFileNames != null && !oldFileNames.isEmpty()){
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            //실제로 파일 삭제
            customFileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT","SUCCESS");
    }

    //상품삭제
    @DeleteMapping("{productId}")
    public Map<String,String> remove(@PathVariable("productId") Long productId){
        //삭제할 파일 알아내기
        List<String> oldFileNames = productService.getProductById(productId).getUploadFileNames();
        productService.deleteProduct(productId);
        customFileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT","DELETE STCCESS");
    }

}
/*
   // 상품 등록
    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@RequestPart("product") ProductDTO product, @RequestPart(value = "images", required = false)List<MultipartFile> images) {
        log.info("Creating product {}", product);
        ProductDTO createProduct = productService.createProduct(product, images );
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }
 */