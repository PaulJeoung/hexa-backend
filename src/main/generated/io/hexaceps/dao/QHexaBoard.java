package io.hexaceps.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHexaBoard is a Querydsl query type for HexaBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHexaBoard extends EntityPathBase<HexaBoard> {

    private static final long serialVersionUID = 442573577L;

    public static final QHexaBoard hexaBoard = new QHexaBoard("hexaBoard");

    public final StringPath author = createString("author");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final BooleanPath visible = createBoolean("visible");

    public QHexaBoard(String variable) {
        super(HexaBoard.class, forVariable(variable));
    }

    public QHexaBoard(Path<? extends HexaBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHexaBoard(PathMetadata metadata) {
        super(HexaBoard.class, metadata);
    }

}

