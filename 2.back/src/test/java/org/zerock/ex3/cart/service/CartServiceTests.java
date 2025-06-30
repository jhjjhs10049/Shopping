package org.zerock.ex3.cart.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex3.cart.dto.AddCartItemDTO;
import org.zerock.ex3.cart.dto.CartItemDTO;
import org.zerock.ex3.cart.dto.ModifyCartItemDTO;

import java.util.List;

@SpringBootTest
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Test
    public void testGetCartList() {
        String mid = "user22";

        List<CartItemDTO> cartList = cartService.getAllItems(mid);

        cartList.forEach(cartItemDTO -> {
            System.out.println(cartItemDTO);
        });
    }

    @Test
    public void testRegisterItem() {

        String mid = "user22";
        Long pno = 40L;
        int qty = 2;

        AddCartItemDTO addCartItemDTO = AddCartItemDTO.builder()
                .holder(mid)
                .pno(pno)
                .quantity(qty)
                .build();

        cartService.registerItem(addCartItemDTO);
    }

    @Test
    public void testModifyItem() {

        Long itemNo = 2L;
        int qty = 1;

        ModifyCartItemDTO modifyCartItemDTO = ModifyCartItemDTO.builder()
                .itemNo(itemNo)
                .quantity(qty)
                .build();

        cartService.modifyItem(modifyCartItemDTO);
    }
}
