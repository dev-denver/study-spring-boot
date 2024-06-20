package com.studyspringboot.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberEntity is a Querydsl query type for MemberEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberEntity extends EntityPathBase<MemberEntity> {

    private static final long serialVersionUID = -578367789L;

    public static final QMemberEntity memberEntity = new QMemberEntity("memberEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final BooleanPath isWithdrawn = createBoolean("isWithdrawn");

    public final StringPath loginId = createString("loginId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberName = createString("memberName");

    public final EnumPath<com.studyspringboot.member.MemberType> memberType = createEnum("memberType", com.studyspringboot.member.MemberType.class);

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public final DateTimePath<java.time.LocalDateTime> withdrawalDatetime = createDateTime("withdrawalDatetime", java.time.LocalDateTime.class);

    public final StringPath withdrawalReason = createString("withdrawalReason");

    public QMemberEntity(String variable) {
        super(MemberEntity.class, forVariable(variable));
    }

    public QMemberEntity(Path<? extends MemberEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberEntity(PathMetadata metadata) {
        super(MemberEntity.class, metadata);
    }

}

