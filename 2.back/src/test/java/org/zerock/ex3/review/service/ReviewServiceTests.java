package org.zerock.ex3.review.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex3.review.dto.ReviewDTO;

@SpringBootTest
public class ReviewServiceTests {
    @Autowired
    private ReviewService reviewService;

    @Test
    public void testRegister(){
        Long pno = 100L;

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewText("리뷰 내용")
                .score(5)
                .reviewer("reviewer1")
                .pno(pno)
                .build();
        reviewService.register(reviewDTO);
    }

    @Test
    public void testRead(){
        Long rno = 1L;
        ReviewDTO reviewDTO = reviewService.read(rno);
        System.out.println(reviewDTO);
    }

    @Test
    public void testDelete(){
        Long rno = 2L;
        ReviewDTO reviewDTO = reviewService.remove(rno);
        System.out.println(reviewDTO);
    }

    @Test
    public void testModify(){
        Long pno = 3L;

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rno(pno) // 반드시 있어야 함
                .score(5)
                .reviewText("수정합니다.")
                .reviewer("reviewer1")
                .build();
        reviewService.modify(reviewDTO);
    }
}
