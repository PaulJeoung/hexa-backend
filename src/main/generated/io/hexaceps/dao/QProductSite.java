package io.hexaceps.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductSite is a Querydsl query type for ProductSite
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProductSite extends BeanPath<ProductSite> {

    private static final long serialVersionUID = -1927157825L;

    public static final QProductSite productSite = new QProductSite("productSite");

    public final StringPath siteLink = createString("siteLink");

    public final NumberPath<Integer> siteOrd = createNumber("siteOrd", Integer.class);

    public QProductSite(String variable) {
        super(ProductSite.class, forVariable(variable));
    }

    public QProductSite(Path<? extends ProductSite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductSite(PathMetadata metadata) {
        super(ProductSite.class, metadata);
    }

}

