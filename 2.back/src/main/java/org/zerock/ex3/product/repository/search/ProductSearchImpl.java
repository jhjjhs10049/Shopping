package org.zerock.ex3.product.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.ex3.product.dto.ProductDTO;
import org.zerock.ex3.product.dto.ProductListDTO;
import org.zerock.ex3.product.entity.ProductEntity;
import org.zerock.ex3.product.entity.QProductEntity;
import org.zerock.ex3.product.entity.QProductImage;
import org.zerock.ex3.review.entity.QReviewEntity;

import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage);

        //where productImage.idx = 0
        query.where(productImage.idx.eq(0));

        //Long pno, String pname, int price, String writer, String productImage
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections.bean(ProductListDTO.class,
                productEntity.pno,
                productEntity.pname,
                productEntity.price,
                productEntity.writer,
                productImage.fileName.as("productImage")));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        java.util.List<ProductListDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listwithAllImages(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        JPQLQuery<ProductEntity> query = from(productEntity);
        this.getQuerydsl().applyPagination(pageable,query);
        List<ProductEntity> entityList = query.fetch();
        long count = query.fetchCount();

        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listFetchAllImages(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage).fetchJoin();

        this.getQuerydsl().applyPagination(pageable, query);

        List<ProductEntity> entityList = query.fetch();

        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();

        long count = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductListDTO> listWithReviewCount(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;
        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(reviewEntity).on(reviewEntity.productEntity.eq(productEntity));
        query.leftJoin(productEntity.images, productImage);

        //where productImage.idx = 0
        query.where(productImage.idx.eq(0));

        this.getQuerydsl().applyPagination(pageable, query);

        //group by
        query.groupBy(productEntity);

        //Long pno, String pname, int price, String writer, String productImage
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections.bean(ProductListDTO.class,
                productEntity.pno,
                productEntity.pname,
                productEntity.price,
                productEntity.writer,
                productImage.fileName.as("productImage"),
                reviewEntity.countDistinct().as("reviewCount")));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        java.util.List<ProductListDTO> dtoList =  dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImagesReviewCount(Pageable pageable) {

        QProductEntity productEntity = QProductEntity.productEntity;
        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(reviewEntity).on(reviewEntity.productEntity.eq(productEntity));

        this.getQuerydsl().applyPagination(pageable, query);

        query.groupBy(productEntity);

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(productEntity, reviewEntity.countDistinct());

        List<Tuple> result = tupleJPQLQuery.fetch();

        List<ProductDTO> dtoList = result.stream().map(tuple -> {
            ProductEntity product = tuple.get(0, ProductEntity.class);
            long count = tuple.get(1, Long.class);

            ProductDTO dto = new ProductDTO(product);

            dto.setReviewCount(count);

            return dto;
        }).toList();

        return new PageImpl<>(dtoList, pageable, tupleJPQLQuery.fetchCount());
    }
}
