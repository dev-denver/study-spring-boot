package com.doongji.member.entity;

import com.doongji.member.MemberType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "member")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberEntity {
  /* PK */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("회원 ID")
  private Long memberId;

  @Column(nullable = false)
  @Comment("이름")
  private String memberName;

  @Column()
  @Comment("로그인 ID")
  private String loginId;

  @Column()
  @Comment("비밀번호")
  @JsonIgnore
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("회원 유형")
  private MemberType memberType;

  @Column(nullable = false)
  @Comment("탈퇴 여부")
  private Boolean isWithdrawn;

  @Column()
  @Comment("탈퇴 사유")
  private String withdrawalReason;

  @Column()
  @Comment("탈퇴 일시")
  private LocalDateTime withdrawalDatetime;

  /* auditing */
  @Column(updatable = false)
  @CreatedDate
  @Comment("생성 일시")
  private LocalDateTime createdAt;

  @Column(updatable = false)
  @CreatedBy
  @Comment("생성자")
  private String createdBy;

  @LastModifiedDate
  @Comment("수정 일시")
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Comment("수정자")
  private String updatedBy;
}
