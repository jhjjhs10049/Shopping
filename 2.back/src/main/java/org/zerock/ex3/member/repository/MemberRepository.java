package org.zerock.ex3.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex3.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,String> {
    
    // 이메일로 회원 찾기
    Optional<MemberEntity> findByEmail(String email);
}
