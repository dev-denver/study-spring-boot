package com.doongji.common.service;

import com.doongji.common.exception.ApplicationException;
import com.doongji.member.MemberType;
import com.doongji.member.entity.MemberEntity;
import com.doongji.member.repository.MemberRepository;
import com.doongji.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String loginId) {
    // find member
    MemberEntity memberEntity =
        this.memberRepository
            .findByLoginIdAndIsWithdrawn(loginId, false)
            .orElseThrow(() -> new ApplicationException(MemberService.NOT_FOUND_MESSAGE));

    // create & return User object
    return createUser(memberEntity.getMemberId(), memberEntity.getPassword(), memberEntity.getMemberType());
  }

  @Transactional
  public UserDetails loadUserByUsernameAndPassword(final String loginId, final String password) {
    // find member
    MemberEntity memberEntity =
        this.memberRepository
            .findByLoginIdAndIsWithdrawn(loginId, false)
            .orElseThrow(() -> new ApplicationException(MemberService.NOT_FOUND_MESSAGE));

    // verify password
    if (!passwordEncoder.matches(password, memberEntity.getPassword())) {
      throw new ApplicationException(MemberService.INVAILD_PASSWORD);
    }

    // create & return User object
    return createUser(
        memberEntity.getMemberId(), memberEntity.getPassword(), memberEntity.getMemberType());
  }

  private User createUser(
      Long memberId, String password, MemberType memberType) {
    // set authorities
    List<GrantedAuthority> grantedAuthorities =
        List.of(new SimpleGrantedAuthority(memberType.name()));
    // userName
    final String username = String.valueOf(memberId);
    // return User object
    return new User(username, password, grantedAuthorities);
  }

}

