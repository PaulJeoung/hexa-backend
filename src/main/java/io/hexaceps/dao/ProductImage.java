package io.hexaceps.dao;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductImage {
    private String fileName;
    private int ord; // 0 이 대표 이미지, 원하는 번호만 볼수 있게 ordering

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
