package org.zerock.ex3.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductListDTO {

    private Long pno;
    private String pname;
    private int price;
    private String writer;
    private String productImage;
    private Long reviewCount;

}
