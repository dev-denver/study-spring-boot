package com.studyspringboot.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  private final TokenProvider tokenProvider;

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {


    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    final String jwt = resolveToken(httpServletRequest);
    final String requestURI = httpServletRequest.getRequestURI();

    if (requestURI.equals("/error")) {
      return;
    }

    boolean isExist = StringUtils.hasText(jwt);
    boolean isValid = this.tokenProvider.validateToken(jwt);

    if (isExist && isValid) {
      Authentication authentication = this.tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      Long memberId = this.tokenProvider.getNullableMemberIdFromAuthentication(authentication);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (bearerToken == null) {
      Cookie[] cookieList = request.getCookies();
      if (cookieList != null) {
        for (Cookie cookie : cookieList) {
          if (this.tokenProvider.LOGIN_JWT_KEY_NAME.equals(cookie.getName())) {
            return cookie.getValue();
          }
        }
      }
    } else {
      if (bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
      } else {
        return bearerToken;
      }
    }
    return null;
  }
}
