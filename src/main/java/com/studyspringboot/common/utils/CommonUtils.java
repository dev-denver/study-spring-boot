package com.studyspringboot.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class CommonUtils {

  public static int getOffset(Pageable pageable) {
    return pageable != null ? pageable.getPageNumber() * pageable.getPageSize() : 0;
  }

  public static int getLimit(Pageable pageable) {
    return pageable != null ? pageable.getPageSize() : 10;
  }

  public static <T> T createInstance(Class<T> clazz) throws Exception {
    Constructor<T> constructor = clazz.getDeclaredConstructor();
    return constructor.newInstance();
  }

  public static <E extends Enum<E>> List<CommonEnumDto<E>> getEnumDtoList(Class<E> enumClass)
      throws Exception {

    List<CommonEnumDto<E>> enumDtoList = new ArrayList<>();

    E[] enumConstantList = enumClass.getEnumConstants();
    for (E enumConstant : enumConstantList) {
      CommonEnumDto.CommonEnumDtoBuilder<E> targetDtoBuilder = CommonEnumDto.builder();
      targetDtoBuilder.enumConstant(enumConstant);
      for (Field enumField : enumConstant.getClass().getDeclaredFields()) {
        enumField.setAccessible(true);
        String sourceDtoFieldName = enumField.getName();
        Object sourceDtoFieldValue = enumField.get(enumConstant);

        if (sourceDtoFieldValue != null) {
          for (Field targetDtoField : targetDtoBuilder.getClass().getDeclaredFields()) {
            targetDtoField.setAccessible(true);
            String targetDtoFieldName = targetDtoField.getName();

            if (targetDtoFieldName.equals(sourceDtoFieldName)) {
              targetDtoField.set(targetDtoBuilder, sourceDtoFieldValue);
              break;
            }
          }
        }
      }
      enumDtoList.add(targetDtoBuilder.build());
    }

    return enumDtoList;
  }
}
