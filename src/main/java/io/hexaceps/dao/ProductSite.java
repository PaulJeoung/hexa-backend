package io.hexaceps.dao;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString @Builder
@AllArgsConstructor @NoArgsConstructor

public class ProductSite {

    private String siteLink;

    private int siteOrd; // 0 = hexaceps, 1 = kream, 2 = stockx, 3 = amazon

    // site sorting 순서 정리
    public void setSiteOrd(int ord) {
        this.siteOrd = siteOrd;
    }

}

