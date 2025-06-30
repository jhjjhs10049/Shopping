package org.zerock.ex3.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.ex3.cart.dto.AddCartItemDTO;
import org.zerock.ex3.cart.dto.CartItemDTO;
import org.zerock.ex3.cart.dto.ModifyCartItemDTO;
import org.zerock.ex3.cart.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    //Gradle 환경 주의
    @PreAuthorize("authentication.name == #addCartItemDTO.holder")
    @PostMapping("/addItem")
    public ResponseEntity<List<CartItemDTO>> addItem(
            @RequestBody AddCartItemDTO addCartItemDTO){
                log.info("add item...............");
                cartService.registerItem(addCartItemDTO);
        List<CartItemDTO> cartItemList = cartService.getAllItems(addCartItemDTO.getHolder());

        return ResponseEntity.ok(cartItemList);
    }

    @GetMapping("/{cno}")
    public ResponseEntity<List<CartItemDTO>> getCArtItemList(
            @PathVariable("cno") Long cno,
            Principal principal) {

        log.info("get cart...................." + cno);

        String mid = principal.getName();

        cartService.checkCartHolder(mid,cno);

        List<CartItemDTO> cartList = cartService.getAllItems(mid);

        return ResponseEntity.ok(cartList);

    }

    @PutMapping("/modifitem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> modifyItem(
            @PathVariable("itemNo") Long itemNo,
            @RequestBody ModifyCartItemDTO modifyCartItemDTO,
            Principal principal) {

        log.info("modify item..............." + modifyCartItemDTO);

        String mid = principal.getName();

        cartService.checkItemHolder(mid, itemNo);

        modifyCartItemDTO.setItemNo(itemNo);

        cartService.modifyItem(modifyCartItemDTO);

        List<CartItemDTO> cartList = cartService.getAllItems(mid);

        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/removeitem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> removeItem(
            @PathVariable ("itemNo") Long itemNo,
            Principal principal) {
        log.info("remove item................" + itemNo);

        String mid = principal.getName();

        cartService.checkItemHolder(mid,itemNo);

        cartService.modifyItem(
                ModifyCartItemDTO.builder().itemNo(itemNo).quantity(0).build()
        );

        List<CartItemDTO> cartList = cartService.getAllItems(mid);

        return ResponseEntity.ok(cartList);
    }
}
