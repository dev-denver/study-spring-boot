package com.studyspringboot.common.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommonEnumDto<E> {

  private E enumConstant;
  private String code;
  private String codeName;
  private String description;
}
