package org.zerock.ex3.product.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.ex3.product.dto.ProductDTO;
import org.zerock.ex3.product.dto.ProductListDTO;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);

    Page<ProductDTO> listwithAllImages(Pageable pageable);

    Page<ProductDTO> listFetchAllImages(Pageable pageable);

    Page<ProductListDTO> listWithReviewCount(Pageable pageable);

    Page<ProductDTO> listWithAllImagesReviewCount(Pageable pageable);
}
