package com.doongji.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateUtils {
  /** 기준 종료 일자 */
  public static final LocalDateTime END_OF_TIME =
      LocalDateTime.parse("99991231000000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

  /** 조회일자 기준 첫날 */
  public static LocalDate getFirstDayOfMonth(final String inquiryDate) {
    return parseLocalDate(inquiryDate).withDayOfMonth(1);
  }

  /** 조회일자 기준 마지막 날 */
  public static LocalDate getLastDayOfMonth(final String inquiryDate) {
    return parseLocalDate(inquiryDate).withDayOfMonth(parseLocalDate(inquiryDate).lengthOfMonth());
  }

  /** String -> LocalDate parsing */
  public static LocalDate parseLocalDate(String inquiryDate) {
    String formatDate = inquiryDate.replaceAll("\\D", "");
    try {
      return LocalDate.parse(formatDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format: " + inquiryDate, e);
    }
  }
}
