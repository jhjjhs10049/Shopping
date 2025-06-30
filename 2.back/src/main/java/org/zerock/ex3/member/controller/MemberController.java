package org.zerock.ex3.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex3.member.dto.MemberDTO;
import org.zerock.ex3.member.service.MemberService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;      
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody MemberDTO memberDTO) {
        log.info("Member register request: " + memberDTO.getMid());

        try {
            MemberDTO result = memberService.register(
                memberDTO.getMid(), 
                memberDTO.getMpw(),
                memberDTO.getEmail(),
                memberDTO.getMname()
            );
            
            return ResponseEntity.ok(
                Map.of("result", "success", "mid", result.getMid(), "message", "회원가입이 완료되었습니다.")
            );        } catch (org.zerock.ex3.member.exception.MemberTaskException e) {

            // 비즈니스 로직 예외 처리
            log.warn("Business logic error: " + e.getMessage());
            String errorMessage;
            int statusCode = e.getCode();
              switch (e.getMsg()) {
                case "DUPLICATE":
                    errorMessage = "이미 사용중인 아이디입니다. 다른 아이디를 사용해주세요.";
                    break;
                case "DUPLICATE_EMAIL":
                    errorMessage = "이미 사용중인 이메일입니다. 다른 이메일을 사용해주세요.";
                    break;
                case "INVALID":
                    errorMessage = "모든 필드를 입력해주세요. (아이디, 비밀번호, 이메일, 이름)";
                    break;
                default:
                    errorMessage = "회원가입 중 오류가 발생했습니다: " + e.getMsg();
            }
            
            return ResponseEntity.status(statusCode)
                .body(Map.of("error", errorMessage, "type", e.getMsg().toLowerCase()));        } catch (RuntimeException e) {
            // 서비스에서 발생한 런타임 예외 (구체적인 메시지 포함)
            log.error("Runtime error during registration: " + e.getMessage());
            String errorType = "validation_error";
            
            // 에러 메시지에 따라 타입 분류
            if (e.getMessage().contains("이미 사용중인 아이디")) {
                errorType = "duplicate_id";
            } else if (e.getMessage().contains("이미 사용중인 이메일")) {
                errorType = "duplicate_email";
            } else if (e.getMessage().contains("입력해주세요") || 
                       e.getMessage().contains("형식") || 
                       e.getMessage().contains("자 이상") ||
                       e.getMessage().contains("자 이하")) {
                errorType = "validation_error";
            }
            
            return ResponseEntity.status(400)
                .body(Map.of("error", e.getMessage(), "type", errorType));
        } catch (Exception e) {
            log.error("Registration failed: " + e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(Map.of("error", "서버 내부 오류가 발생했습니다.", "type", "server_error"));
        }
    }
}
