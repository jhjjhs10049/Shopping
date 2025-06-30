package org.zerock.ex3.review.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex3.product.dto.PageRequestDTO;
import org.zerock.ex3.product.dto.ProductDTO;
import org.zerock.ex3.product.dto.ProductListDTO;
import org.zerock.ex3.product.entity.ProductEntity;
import org.zerock.ex3.review.entity.ReviewEntity;
import org.zerock.ex3.review.exception.ReviewExceptions;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            Long pno = 51L;

            ProductEntity productEntity = ProductEntity.builder().pno(pno).build();

            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .reviewText("리뷰 내용....")
                    .score(5)
                    .reviewer("reviewer1")
                    .productEntity(productEntity)
                    .build();

            reviewRepository.save(reviewEntity);
        }
    }

    @Transactional
    @Test
    public void testRead() {
        Long rno = 1L;

        reviewRepository.findById(rno).ifPresent(reviewEntity -> {
            System.out.println(reviewEntity);
            System.out.println(reviewEntity.getProductEntity());
        });
    }

//    @Transactional
    @Test
    public void testGetWithProduct() {
        Long rno = 1L;

        reviewRepository.getWithProduct(rno).ifPresent(reviewEntity -> {
            System.out.println(reviewEntity);
            System.out.println(reviewEntity.getProductEntity());
            System.out.println(reviewEntity.getProductEntity().getImages());
        });
    }

    @Transactional
    @Test
    @Commit
    public void testRemove(){
        Long rno = 1L;

        reviewRepository.deleteById(rno);
    }

    @Transactional
    @Test
    @Commit
    public void testUpdate() {
        Long rno =2L;

        ReviewEntity reviewEntity =
                reviewRepository.findById(rno).orElseThrow(ReviewExceptions.REVIEW_NOT_FOUND::get);
        reviewEntity.changeReviewText("변경된 리뷰 내용");
        reviewEntity.changeScore(3);
    }

    @Test
    public void testList(){

        Long pno = 51L;

        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());

        reviewRepository.getListByPno(pno, pageable).getContent().forEach(reviewDTO -> {
            System.out.println(reviewDTO);
        });
    }
}
