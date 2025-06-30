package org.zerock.ex3.cart.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartTaskException extends RuntimeException{

    private String message;
    private int status;

    public CartTaskException(String message, int status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public static enum Items{

        NOT_FOUND_CARTITEM("Cannot find CartItem", 404),
        NOT_FOUND_CART("Cannot find Cart", 404),
        NOT_FOUND_PRODUCT("Cannot find Product", 404),
        INVALID_QUANTITY("Invalid Quantity", 400),
        DUPLICATE_PRODUCT("Duplicate Product In Cart", 400),
        CART_ITEM_REGISTER_FAIL("Cart Register Fail", 500),
        CART_ITEM_UPDATE_FAIL("Cart Update Fail", 500),
        CART_ITEM_DELETE_FAIL("Cart Delete Fail", 500),
        NOT_CARTITEM_OWNER("Not CartItem Owner", 403);

        private String message;
        private int status;

        Items(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public CartTaskException value() {
            return new CartTaskException(this.message, this.status);
        }

    }
}
