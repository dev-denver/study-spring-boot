package com.doongji.common.web;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAuditorAware implements AuditorAware<String> {
  // 회원관리를 편하게 할 수 있도록 Bean 추가
  @Override
  public Optional<String> getCurrentAuditor() {
    String memberId = "doongji";
    return Optional.of(memberId);
  }
}
