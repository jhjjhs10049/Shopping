package org.zerock.ex3.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex3.member.dto.MemberDTO;
import org.zerock.ex3.member.entity.MemberEntity;
import org.zerock.ex3.member.exception.MemberExceptions;
import org.zerock.ex3.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberDTO read(String mid, String mpw) {

        Optional<MemberEntity> result = memberRepository.findById(mid);

        if (!result.isPresent()) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }

        MemberEntity memberEntity = result.get();

        if (!passwordEncoder.matches(mpw, memberEntity.getMpw())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return new MemberDTO(memberEntity);
    }

    public MemberDTO getByMid(String mid) {

        Optional<MemberEntity> result = memberRepository.findById(mid);

        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        return new MemberDTO(memberEntity);
    }    // 회원가입 메서드 추가
    public MemberDTO register(String mid, String mpw, String email, String mname) {
        log.info("Registering member: " + mid);        // 입력값 검증
        if (mid == null || mid.trim().isEmpty()) {
            throw new RuntimeException("아이디를 입력해주세요.");
        }
        
        if (mpw == null || mpw.trim().isEmpty()) {
            throw new RuntimeException("비밀번호를 입력해주세요.");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("이메일을 입력해주세요.");
        }
        
        if (mname == null || mname.trim().isEmpty()) {
            throw new RuntimeException("이름을 입력해주세요.");
        }

        // 상세 조건 검증
        if (mid.trim().length() < 3 || mid.trim().length() > 20) {
            throw new RuntimeException("아이디는 3자 이상 20자 이하로 입력해주세요.");
        }
        
        if (mpw.length() < 4) {
            throw new RuntimeException("비밀번호는 4자 이상 입력해주세요.");
        }
        
        // 이메일 형식 검증 (간단한 정규식)
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("올바른 이메일 형식을 입력해주세요.");
        }
        
        if (mname.trim().length() < 2 || mname.trim().length() > 10) {
            throw new RuntimeException("이름은 2자 이상 10자 이하로 입력해주세요.");
        }

        // 중복 검사 - 입력값이 유효한 경우에만 실행
        Optional<MemberEntity> existingMemberById = memberRepository.findById(mid.trim());
        if (existingMemberById.isPresent()) {
            log.warn("Duplicate ID attempted: " + mid);
            throw MemberExceptions.DUPLICATE.get();
        }

        Optional<MemberEntity> existingMemberByEmail = memberRepository.findByEmail(email.trim());
        if (existingMemberByEmail.isPresent()) {
            log.warn("Duplicate email attempted: " + email);
            throw MemberExceptions.DUPLICATE_EMAIL.get();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(mpw);

        // 새 멤버 엔티티 생성
        MemberEntity memberEntity = MemberEntity.builder()
                .mid(mid)
                .mpw(encodedPassword)
                .email(email)
                .mname(mname)
                .role("USER") // 기본 역할
                .build();

        // 저장
        MemberEntity savedEntity = memberRepository.save(memberEntity);
        log.info("Member registered successfully: " + savedEntity.getMid());

        return new MemberDTO(savedEntity);
    }
}
