package com.studyspringboot.common.utils;

import org.springframework.util.StringUtils;

public class CustomStringUtils {

  /**
   * 첫 글자와 마지막 글자를 제외한 나머지 문자를 모두 masking
   *
   * @param originalString raw string
   * @return masked string
   */
  public static String doMasking(String originalString) {
    if (!StringUtils.hasText(originalString)) {
      return "";
    }

    if (originalString.length() == 1) {
      return originalString;
    }

    return originalString.charAt(0)
        + "*".repeat(originalString.length() - 2)
        + originalString.charAt(originalString.length() - 1);
  }
}
