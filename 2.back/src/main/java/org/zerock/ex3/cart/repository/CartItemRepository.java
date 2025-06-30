package org.zerock.ex3.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex3.cart.entity.CartItemEntity;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    @Query("select c" +
    " from CartItemEntity c " +
    " join fetch c.product " +
    " join fetch c.product.images " +
    " where c.cart.holder = :holder" +
    " order by c.itemNo desc ")
    Optional<List<CartItemEntity>> getCartItemsOfHOLDER(@Param("holder") String holder);

    @Query("select c, p, pi" +
            " from CartItemEntity c " +
            " join fetch c.product p " +
            " join fetch c.product.images pi " +
            " where c.cart.holder = :holder" +
            " and pi.idx = 0" +
            " order by c.itemNo desc ")
    List<Object[]> getCartItemsOfHolder2(@Param("holder") String holder);

    @Query("select c.cart.holder " +
            "from CartItemEntity c " +
            " where c.itemNo = :itemNo" )
    Optional<String> getHolderOfCartItem(@Param("itemNo") Long itemNo);
}
