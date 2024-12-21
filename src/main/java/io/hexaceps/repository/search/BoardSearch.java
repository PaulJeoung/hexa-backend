package io.hexaceps.repository.search;

import io.hexaceps.dao.HexaBoard;
import io.hexaceps.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<HexaBoard> pageSearch(String category, PageRequestDTO pageRequestDTO);

}
