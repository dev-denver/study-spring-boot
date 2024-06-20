package com.studyspringboot.common.utils;

import java.lang.reflect.Field;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EntityUtils {
  /**
   * entity 수정
   *
   * @param entity target entity
   * @param dto 변경 값 DTO
   */
  public static void updateEntity(Object entity, Object dto) {
    try {
      for (Field dtoField : dto.getClass().getDeclaredFields()) {
        // get dto field info
        dtoField.setAccessible(true);
        String dtoFieldName = dtoField.getName();
        Object dtoFieldValue = dtoField.get(dto);

        if ("password".equals(dtoFieldName) && dtoFieldValue != null) {
          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          dtoFieldValue = passwordEncoder.encode(dtoFieldValue.toString());
        }

        // update entity field value
        if (dtoFieldValue != null) {
          for (Field entityField : entity.getClass().getDeclaredFields()) {
            entityField.setAccessible(true);
            String entityFieldName = entityField.getName();
            Object entityFieldValue = entityField.get(entity);

            if (entityFieldName.equals(dtoFieldName)) {
              entityField.set(entity, dtoFieldValue);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
