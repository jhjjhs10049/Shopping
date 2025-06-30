package org.zerock.ex3.product.repository;

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
import org.zerock.ex3.product.dto.ProductDTO;
import org.zerock.ex3.product.dto.ProductListDTO;
import org.zerock.ex3.product.entity.ProductEntity;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Commit
    public void testInsert(){
        for (int i = 1; i <=50; i++){

            ProductEntity productEntity = ProductEntity.builder()
                    .pname(i+"_새로운 상품")
                    .price(5000)
                    .content(i+"_상품 설명")
                    .writer("user00")
                    .build();

            productEntity.addImage(i+"_test1.jpg");
            productEntity.addImage(i+"_test2.jpg");

            productRepository.save(productEntity);

            System.out.println("New Product no : " + productEntity.getPno());
        }
    }

    @Test
    @Transactional(readOnly = true)
    public void testRead() {
        Long pno = 1L;

        Optional<ProductEntity> result = productRepository.findById(pno);

        ProductEntity productEntity = result.get();

        System.out.println(productEntity);

        System.out.println("---------------------------");

        System.out.println(productEntity.getImages());
    }

    @Test
    public void testReadQuery(){
        Long pno = 1L;

        Optional<ProductEntity> result = productRepository.getProduct(pno);

        ProductEntity productEntity = result.get();

        System.out.println(productEntity);

        System.out.println("---------------------------");

        System.out.println(productEntity.getImages());
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        Optional<ProductEntity> result = productRepository.getProduct(1L);

        ProductEntity productEntity = result.get();

        productEntity.changeTitle("변경된 상품");

        productEntity.changePrice(10000);

        productEntity.addImage("new1.jpg");

        productEntity.addImage("new2.jpg");

        //변경 감지시에는 필요없음
        //productRepoistory.save(productEntity);

    }

    @Test
    @Transactional
    @Commit
    public void testDelete(){
        productRepository.deleteById(1L);
    }

    @Test
    public void testReadDTO(){
        Long pno = 10L; //반드시 db에 있는 번홀로

        Optional<ProductDTO> result = productRepository.getProductDTO(pno);

        ProductDTO productDTO = result.get();

        System.out.println(productDTO);
    }

    @Test
    public void testList(){

        org.springframework.data.domain.Pageable pageable
                = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductListDTO> result = productRepository.list(pageable);

        result.getContent().forEach(productListDTO -> {
            System.out.println(productListDTO);
        });
    }

    @Transactional
    @Test
    public void testListWithAllImages() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno"). descending());
        Page<ProductDTO> result = productRepository.listwithAllImages(pageable);

        result.getContent().forEach(productDTO -> {
            System.out.println(productDTO);
        });
    }

    @Test
    public void testListFetchAllImages(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("pno").descending());

        Page<ProductDTO> result = productRepository.listFetchAllImages(pageable);

        for (ProductDTO productDTO : result.getContent()) {
            System.out.println(productDTO);
        }
    }

    @Test
    public void testListWithReviewCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<ProductListDTO> result = productRepository.listWithReviewCount(pageable);

        result.getContent().forEach(productListDTO -> {
            System.out.println(productListDTO);
        });
    }

    @Transactional
    @Test
    public void testListWithAllImagesReviewCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno"). descending());

        Page<ProductDTO> result = productRepository.listWithAllImagesReviewCount(pageable);

        result.getContent().forEach(productDTO -> {
            System.out.println(productDTO);
        });
    }
}
