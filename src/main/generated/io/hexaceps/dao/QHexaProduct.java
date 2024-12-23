package io.hexaceps.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHexaProduct is a Querydsl query type for HexaProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHexaProduct extends EntityPathBase<HexaProduct> {

    private static final long serialVersionUID = -249985966L;

    public static final QHexaProduct hexaProduct = new QHexaProduct("hexaProduct");

    public final StringPath category = createString("category");

    public final ListPath<ProductImage, QProductImage> imageList = this.<ProductImage, QProductImage>createList("imageList", ProductImage.class, QProductImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath productBrand = createString("productBrand");

    public final StringPath productDescription = createString("productDescription");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> productStock = createNumber("productStock", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> registeredAt = createDateTime("registeredAt", java.time.LocalDateTime.class);

    public final ListPath<ProductSite, QProductSite> siteList = this.<ProductSite, QProductSite>createList("siteList", ProductSite.class, QProductSite.class, PathInits.DIRECT2);

    public final StringPath size = createString("size");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QHexaProduct(String variable) {
        super(HexaProduct.class, forVariable(variable));
    }

    public QHexaProduct(Path<? extends HexaProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHexaProduct(PathMetadata metadata) {
        super(HexaProduct.class, metadata);
    }

}

