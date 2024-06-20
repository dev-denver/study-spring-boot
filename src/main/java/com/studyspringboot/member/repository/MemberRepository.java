package com.studyspringboot.member.repository;

import com.studyspringboot.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberCustomRepository {

  Optional<MemberEntity> findByLoginIdAndIsWithdrawn(String loginId, boolean isWithDrawn);
}
