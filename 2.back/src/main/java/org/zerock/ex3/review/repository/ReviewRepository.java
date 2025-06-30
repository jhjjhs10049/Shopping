package org.zerock.ex3.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex3.review.dto.ReviewDTO;
import org.zerock.ex3.review.entity.ReviewEntity;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("select r from ReviewEntity r " +
            " join fetch  r.productEntity rp " +
            " join fetch rp.images " +
            " where r.rno= :rno")
    Optional<ReviewEntity> getWithProduct( @Param("rno") Long rno);

    @Query("select r from ReviewEntity r where r.productEntity.pno = :pno")
    Page<ReviewDTO> getListByPno(@Param("pno") Long pno, Pageable pageable);
}
