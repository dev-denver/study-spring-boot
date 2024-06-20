package com.studyspringboot.common.utils;

import com.studyspringboot.common.dto.Response;
import com.studyspringboot.common.exception.ApplicationException;
import com.studyspringboot.common.message.StatusMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.lang.reflect.Field;
import java.util.List;

public class DtoUtils {

  // TODO: 상속 받은 DTO(parent DTO) 에 정의된 field 까지 처리할 수 있도록 개선
  public static <T> T copyDto(Object sourceDto, T targetDto) {

    if (sourceDto == null) {
      return null;
    }

    try {
      for (Field sourceDtoField : sourceDto.getClass().getDeclaredFields()) {
        sourceDtoField.setAccessible(true);
        String sourceDtoFieldName = sourceDtoField.getName();
        Object sourceDtoFieldValue = sourceDtoField.get(sourceDto);

        if (sourceDtoFieldValue != null) {
          for (Field targetDtoField : targetDto.getClass().getDeclaredFields()) {
            targetDtoField.setAccessible(true);
            String targetDtoFieldName = targetDtoField.getName();

            if (targetDtoFieldName.equals(sourceDtoFieldName)) {
              targetDtoField.set(targetDto, sourceDtoFieldValue);
              break;
            }
          }
        }
      }

      return targetDto;
    } catch (Exception e) {
      throw new ApplicationException(e.toString());
    }
  }

  public static <S, T> List<T> copyDtoList(List<S> sourceDtoList, Class<T> targetDtoClass) {

    if (sourceDtoList == null || sourceDtoList.isEmpty()) {
      return null;
    }

    return sourceDtoList.stream()
        .map(
            sourceDto -> {
              try {
                T targetDtoInstance = CommonUtils.createInstance(targetDtoClass);
                return DtoUtils.copyDto(sourceDto, targetDtoInstance);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .toList();
  }

  /**
   * convert DTO to String
   *
   * @param dto 변환 대상 DTO
   * @return String
   */
  public static String convertDtoToString(Object dto) {
    try {
      ObjectMapper objectMapper = DtoUtils.getCommonObjectMapper();
      return objectMapper.writeValueAsString(dto);
    } catch (Exception e) {
      throw new RuntimeException("dto utils - convert DTO to String");
    }
  }

  /** */
  public static ObjectMapper getCommonObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper;
  }

  public static <T> Response<T> createResponse(
      String responseCode, String responseMessage, T result) {
    return Response.<T>builder()
        .responseCode(responseCode)
        .responseMessage(responseMessage)
        .result(result)
        .build();
  }

  public static Response<?> createResponse(String responseCode, String responseMessage) {
    return Response.builder().responseCode(responseCode).responseMessage(responseMessage).build();
  }

  public static <T> Response<T> createResponse(T result) {
    return Response.<T>builder()
        .responseCode(StatusMessage.SUCCESS)
        .responseMessage(StatusMessage.SUCCESS_MESSAGE)
        .result(result)
        .build();
  }

  public static <T> Response<T> createResponse() {
    return Response.<T>builder()
        .responseCode(StatusMessage.SUCCESS)
        .responseMessage(StatusMessage.SUCCESS_MESSAGE)
        .build();
  }
}
