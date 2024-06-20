package com.doongji.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberType {

  EMPLOYEE("employee", "둥지직원"),
  LESSEE("lessee", "임차인"),
  OWNER("owner", "건물주"),
  OUTSOURCING("outsourcing", "협력업체");

  private final String code;
  private final String description;

  public static final String VALIDATION_MESSAGE = "회원 유형을 확인해 주세요";

}
