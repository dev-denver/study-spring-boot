package com.doongji.member.service;

import com.doongji.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  public static final String NOT_FOUND_MESSAGE = "일치하는 회원 정보를 찾을 수 없습니다.";
  public static final String INVAILD_PASSWORD = "비밀번호가 틀렸습니다.";


  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

}
