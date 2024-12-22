package io.hexaceps.repository.search;

import io.hexaceps.dto.PageRequestDTO;
import io.hexaceps.dto.PageResponseDTO;
import io.hexaceps.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO requestDTO);

}
