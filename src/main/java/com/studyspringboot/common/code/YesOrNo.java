package com.studyspringboot.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 여부 플레그
 *
 * <pre> 여러 곳에서 사용 </pre>
 */
@Getter
@AllArgsConstructor
public enum YesOrNo {

  /** YES - TRUE - 여 : 1 */
  YES("1", "Y", true),
  /** NO - FALSE - 부 : 0 */
  NO("0", "N", false);

  private final String code;
  private final String value;
  private final boolean flag;

  /**
   * YES - TRUE - 여 : 1
   *
   * <pre>코드 체크</pre>
   *
   * @return boolean
   */
  public static boolean isYes(String code) {
    return YES.code.equals(code) || YES.value.equals(code);
  }

  /**
   * NO - FALSE - 부 : 0
   *
   * <pre>코드 체크</pre>
   *
   * @return boolean
   */
  public static boolean isNo(String code) {
    return NO.code.equals(code) || NO.value.equals(code);
  }
}
