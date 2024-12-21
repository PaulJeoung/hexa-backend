package io.hexaceps.repository.search;

import io.hexaceps.dao.HexaBoard;
import io.hexaceps.dto.PageRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/*
    //querydsl, 5.0.0은 버전임
	implementation "com.querydsl:querydsl-jpa:5.0.0:jakarta"
	annotationProcessor(
			"jakarta.persistence:jakarta.persistence-api",
			"jakarta.annotation:jakarta.annotation-api",
			"com.querydsl:querydsl-apt:5.0.0:jakarta")
 */
@Slf4j
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(HexaBoard.class);
    }

    @Override
    public Page<HexaBoard> pageSearch(String category, PageRequestDTO pageRequestDTO) {
        log.info("pageSearch called");

        return null;
    }
}
