package com.studyspringboot.login.controller;

import com.studyspringboot.common.dto.Response;
import com.studyspringboot.common.service.CookieService;
import com.studyspringboot.jwt.TokenProvider;
import com.studyspringboot.login.dto.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.studyspringboot.common.utils.DtoUtils.createResponse;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "로그인")
public class LoginController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;

  @Operation(summary = "ID PW 로그인")
  @PostMapping("/v1/login")
  public Response<?> login(
      @RequestBody @Valid LoginRequestDto request, HttpServletResponse httpResponse) {

    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

    // .authenticate() -> call CustomUserDetailsService.loadUserByUsername()
    Authentication authenticationObj = authenticationManagerBuilder.getObject().authenticate(token);

    // set auth info on Security Context
    SecurityContextHolder.getContext().setAuthentication(authenticationObj);

    // create JWT
    final String loginJwt = tokenProvider.createToken(authenticationObj);

    // set JWT on header
    ResponseCookie loginJwtCookie =
        CookieService.createJwtCookie(
            this.tokenProvider.LOGIN_JWT_KEY_NAME, loginJwt, "/", true, true, "None");

    httpResponse.setHeader("Set-Cookie", loginJwtCookie.toString());

    return createResponse();
  }


  @Operation(summary = "로그아웃")
  @GetMapping("/v1/log-out")
  public Response<?> logout(Authentication authentication, HttpServletResponse httpResponse) {
    // create JWT
    String expiredJwt = tokenProvider.expireToken(authentication);

    // set JWT on header
    ResponseCookie expiredJwtCookie =
        CookieService.createJwtCookie(
            this.tokenProvider.LOGIN_JWT_KEY_NAME, expiredJwt, "/", true, true, "None");
    httpResponse.setHeader("Set-Cookie", expiredJwtCookie.toString());

    // return
    return createResponse();
  }
}
