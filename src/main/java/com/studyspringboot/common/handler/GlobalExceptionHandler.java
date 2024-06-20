package com.studyspringboot.common.handler;


import com.studyspringboot.common.dto.Response;
import com.studyspringboot.common.exception.ApplicationException;
import com.studyspringboot.common.message.StatusMessage;
import com.studyspringboot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public Response<?> handleBadCredentialsException(final BadCredentialsException e) {
    Response<?> response = new Response<>();
    response.setResponseCode(StatusMessage.FAIL);
    response.setResponseMessage(MemberService.NOT_FOUND_MESSAGE);
    return response;
  }

  @ExceptionHandler(BindException.class)
  public Response<?> handleBindException(final BindException e) {
    Response<?> response = new Response<>();
    response.setResponseCode(StatusMessage.FAIL);
    response.setResponseMessage(e.toString());
    return response;
  }

  @ExceptionHandler(ApplicationException.class)
  public Response<?> handleApplicationException(final ApplicationException e) {
    Response<?> response = new Response<>();
    response.setResponseCode(StatusMessage.FAIL);
    response.setResponseMessage(e.getMessage());
    return response;
  }

  @ExceptionHandler(RuntimeException.class)
  public Response<?> handleRuntimeException(final RuntimeException e) {
    Response<?> response = new Response<>();
    response.setResponseCode(StatusMessage.FAIL);
    response.setResponseMessage(e.getMessage());
    return response;
  }
}
