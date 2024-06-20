package com.doongji.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

  public static ResponseCookie createJwtCookie(
      String keyName, String jwt, String path, Boolean secure, Boolean httpOnly, String sameSite) {

    return ResponseCookie.from(keyName, jwt)
        .path(path)
        .secure(secure)
        .httpOnly(httpOnly)
        .sameSite(sameSite)
        .build();
  }
}
