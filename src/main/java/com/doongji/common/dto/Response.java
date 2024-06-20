package com.doongji.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response<R> {

  @NotBlank private String responseCode;
  @NotBlank private String responseMessage;
  private R result;

  public Response() {}

  public Response(String responseCode, String responseMessage, R result) {
    this.responseCode = responseCode;
    this.responseMessage = responseMessage;
    this.result = result;
  }
}
