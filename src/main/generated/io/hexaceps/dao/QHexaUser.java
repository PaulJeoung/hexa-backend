package io.hexaceps.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHexaUser is a Querydsl query type for HexaUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHexaUser extends EntityPathBase<HexaUser> {

    private static final long serialVersionUID = -816437432L;

    public static final QHexaUser hexaUser = new QHexaUser("hexaUser");

    public final NumberPath<Integer> activate = createNumber("activate", Integer.class);

    public final StringPath address = createString("address");

    public final NumberPath<Integer> agree = createNumber("agree", Integer.class);

    public final StringPath email = createString("email");

    public final StringPath favourite = createString("favourite");

    public final BooleanPath newsletter = createBoolean("newsletter");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ComparablePath<Character> rank = createComparable("rank", Character.class);

    public final DatePath<java.time.LocalDate> registeredAt = createDate("registeredAt", java.time.LocalDate.class);

    public final StringPath size = createString("size");

    public final BooleanPath socialLogin = createBoolean("socialLogin");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath username = createString("username");

    public final ListPath<UserRole, EnumPath<UserRole>> userRoleList = this.<UserRole, EnumPath<UserRole>>createList("userRoleList", UserRole.class, EnumPath.class, PathInits.DIRECT2);

    public QHexaUser(String variable) {
        super(HexaUser.class, forVariable(variable));
    }

    public QHexaUser(Path<? extends HexaUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHexaUser(PathMetadata metadata) {
        super(HexaUser.class, metadata);
    }

}

