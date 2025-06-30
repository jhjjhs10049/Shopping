package org.zerock.ex3.sample.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/v1/samples")
public class SampleController {

    @PreAuthorize("hasRole('ROLE_USER')")
    //매서드 실행전 관리자권환확인
    @GetMapping("/list")
    public ResponseEntity<?> list() {
        log.info("list..............");
        String[] arr = {"AAA", "BBB", "CCC"};
        return ResponseEntity.ok(arr);
    }
}
