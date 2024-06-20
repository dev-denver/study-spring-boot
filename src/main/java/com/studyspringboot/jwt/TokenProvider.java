package com.studyspringboot.jwt;

import com.studyspringboot.member.MemberType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider implements InitializingBean {

  public final String LOGIN_JWT_KEY_NAME;
  private final String AUTHORITIES_KEY_NAME;
  private final String JWT_SECRET;
  private final long TOKEN_VALIDITY_IN_MILLI_SECONDS;
  private Key key;

  private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

  public TokenProvider(
      @Value("${jwt.key-name}") String keyName,
      @Value("${jwt.authorities-key-name}") String authKeyName,
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
    this.LOGIN_JWT_KEY_NAME = keyName;
    this.AUTHORITIES_KEY_NAME = authKeyName;
    this.JWT_SECRET = secret;
    this.TOKEN_VALIDITY_IN_MILLI_SECONDS = tokenValidityInSeconds * 1000; // TODO : 개발완료 후 시간 변경
  }

  @Override
  public void afterPropertiesSet() {
    byte[] keyBytes = Decoders.BASE64.decode(this.JWT_SECRET);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(Authentication authenticationObj) {
    // authorities
    String authorities =
        authenticationObj.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    // expiration date
    long now = (new Date()).getTime();
    Date validity = new Date(now + this.TOKEN_VALIDITY_IN_MILLI_SECONDS);

    // return JWT
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setSubject(authenticationObj.getName()) // member ID
        .claim(AUTHORITIES_KEY_NAME, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(validity)
        .compact();
  }

  public String createTokenWithoutAuthObject(MemberType memberType, Long memberId) {

    // expiration date //TODO : 개발 완료 후 시간 수정 필요
    long now = (new Date()).getTime();
    Date validity = new Date(now + this.TOKEN_VALIDITY_IN_MILLI_SECONDS);

    // return JWT
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setSubject(memberId.toString())
        .claim(AUTHORITIES_KEY_NAME, memberType.name())
        .signWith(key, SignatureAlgorithm.HS512)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY_NAME).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public Long getNullableMemberIdFromAuthentication(Authentication authentication) {

    if (authentication == null) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (principal == null) {
      return null;
    }

    String memberId = null;
    if (principal instanceof User) {
      memberId = ((User) principal).getUsername();
    } else if (principal instanceof Principal) {
      memberId = ((Principal) principal).getName();
    }

    return Optional
        .ofNullable(memberId).map(Long::parseLong).orElse(null);
  }

  public Boolean validateToken(String token) throws Error {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException
        | MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException e) {
      // TODO: 에러 로그 생성 등 구체적인 기능 필요
      logger.error(e.toString());
      return false;
    }
  }

  public String expireToken(Authentication authenticationObj) {
    String loginId = null;
    String authorities = null;

    if (authenticationObj != null) {
      loginId = authenticationObj.getName();
      authorities =
          authenticationObj.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));
    }

    return Jwts.builder()
        .setSubject(loginId)
        .claim(AUTHORITIES_KEY_NAME, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(new Date())
        .compact();
  }

  public <T> T decodePayload(String token, Class<T> targetClass) {

    String[] tokenParts = token.split("\\.");
    String payloadJWT = tokenParts[1];
    Base64.Decoder decoder = Base64.getUrlDecoder();
    String payload = new String(decoder.decode(payloadJWT));
    ObjectMapper objectMapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      return objectMapper.readValue(payload, targetClass);
    } catch (Exception e) {
      throw new RuntimeException("Error decoding token payload", e);
    }
  }
}
