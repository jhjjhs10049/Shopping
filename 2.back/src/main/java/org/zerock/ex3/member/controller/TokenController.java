package org.zerock.ex3.member.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex3.member.dto.MemberDTO;
import org.zerock.ex3.member.security.util.JWTUtil;
import org.zerock.ex3.member.service.MemberService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/token")
@Log4j2
@RequiredArgsConstructor
public class TokenController {

    private final MemberService memberService;

    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    //make에서 만드는 토큰은 처음 만들어주는 토큰
    public ResponseEntity<Map<String,String>> makeToken(@RequestBody MemberDTO memberDTO) {
        log.info("make token..........\nmid: {}", memberDTO.getMid());
        try {
            MemberDTO memberDTOResult = memberService.read(memberDTO.getMid(), memberDTO.getMpw());
            log.info(memberDTOResult);
            String mid = memberDTOResult.getMid();
            Map<String, Object> dataMap = memberDTOResult.getDataMap();

            //make token 유효기간
            String accessToken = jwtUtil.createToken(dataMap, 60 * 24 * 7);
            String refreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

            log.info("accessToken : " + accessToken);
            log.info("refreshToken : " + refreshToken);
            return ResponseEntity.ok(
                    Map.of("accessToken", accessToken, "refreshToken", refreshToken)
            );
        } catch (RuntimeException e) {
            String errorType = "bad_credentials";
            String errorMsg = "아이디 혹은 비밀번호가 맞지 않습니다.";
            return ResponseEntity.status(401).body(Map.of("type", errorType, "error", errorMsg));
        } catch (Exception e) {
            log.error("[TokenController] 로그인 서버 오류", e);
            return ResponseEntity.status(500).body(Map.of("type", "server_error", "error", "서버 내부 오류가 발생했습니다."));
        }
    }

    @PostMapping("/refresh")

    //1. 클라이언트가 보낸 Access Token과 Refresh Token을 검사.
    //
    //2. Access Token이 유효하면 그대로 반환.
    //
    //3. Access Token이 만료되었으면 Refresh Token으로 새 토큰 발급.
    //
    //4. 모든 에러는 400 Bad Request로 처리.

    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader("Authorization") String accessTokenStr,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid
    ){

        log.info("access token with Bearer............." + accessTokenStr);

        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")){
            return handleException("No Access Token" , 400); //400 Bad Request
        }

        if(refreshToken == null) {
            return handleException("No Refresh Token", 400); //400 Bad Request
        }

        log.info("refresh token.........." + refreshToken);

        if(mid == null) {
            return handleException("No Mid", 400); //400 Bad Request
        }

        //Access Token이 만료되었는지 확인
        String accessToken = accessTokenStr.substring(7); //(Bearer 이렇게 6글자가 있으니 7번째글자부터 토큰임)

        try{
            jwtUtil.validateToken(accessToken);

            //아직 만료 기한이 남아 있는 상황
            Map<String, String> data = makeData(mid, accessToken, refreshToken);

            log.info("Access Token is not expired........................");

            return ResponseEntity.ok(data);

        }catch(ExpiredJwtException expiredJwtException) {

            try {
                //Refresh 가 필요한 상황
                Map<String, String> newTokenMap = makeNewToken(mid, refreshToken);
                return ResponseEntity.ok(newTokenMap);
            } catch (Exception e) {
                return handleException("REFRESH " + e.getMessage(), 400); //400 Bad Request
            }
        }catch(Exception e) {
            e.printStackTrace();//디버깅용
            return handleException(e.getMessage(), 400); //400 Bad Request
        }
    }

    //mid, accessToken, refreshToken 값을 받아서 Map<String, String> 객체 반환
    private Map<String, String> makeData(String mid, String accessToken, String refreshToken) {
        return Map.of("mid", mid, "accessToken", accessToken, "refreshToken", refreshToken);
    }
    //에러 메시지를 담은 JSON 형태의 응답을 반환
    private ResponseEntity<Map<String, String>> handleException(String msg, int status){
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    //makeNewToken은 기본 make에서 만든토큰의 엑세스토큰이 만료되고 리프래쉬 토큰이 남아있는경우 새로 발급해주는 토큰이다.
    private Map<String, String> makeNewToken(String mid, String refreshToken) {
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        log.info("refresh token claims : " + claims);
        if(!mid.equals(claims.get("mid").toString())){
            throw new RuntimeException("Invalid refresh Token Host");
        }
        //mid 를 이용해서 사용자 정보를 다시 확인하 후에 새로운 토큰 생성
        MemberDTO memberDTO = memberService.getByMid(mid);
        Map<String,Object> newClaims = memberDTO.getDataMap();

        //refresh된 토큰 유효기간
        String newAccessToken = jwtUtil.createToken(newClaims, 60 * 24 * 7);
        String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        return makeData(mid, newAccessToken, newRefreshToken);
    }
}


