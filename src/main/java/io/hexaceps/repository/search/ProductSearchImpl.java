package io.hexaceps.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import io.hexaceps.dao.HexaProduct;
import io.hexaceps.dao.QHexaProduct;
import io.hexaceps.dao.QProductImage;
import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QPageRequest;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() { // RequiredArgsConstructor 대신 직접 생성
        super(HexaProduct.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        log.info("Searching query List 동작 확인");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("productId").descending()
        );
        QHexaProduct product = QHexaProduct.hexaProduct;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<HexaProduct> query = from(product); // hexaproduct로 부터 쿼리를 뽑음
        query.leftJoin(product.imageList, productImage); // 상품인데 이미지가 없는 경우가 있을 경우를 생각해 leftjoin
        query.where(productImage.ord.eq(0)); // 0번째 이미지가 가저오게 where절 추가
        Objects.requireNonNull(getQuerydsl().applyPagination(pageable, query)); // 페이징 처리

        List<Tuple> productList2 = query.select(product, productImage).fetch(); // 상품과 이미지를 뽑기 위해서
        long count = query.fetchCount();
        log.info("Found {} products", productList2);

        return null;
    }
}
