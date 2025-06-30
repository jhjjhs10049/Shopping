package org.zerock.ex3.review.exception;

public enum ReviewExceptions {

    REVIEW_NOT_REGISTERED("Review Not Registered", 400),
    REVIEW_PRODUCT_NOT_FOUND("Product Not Found for Review", 404),

    REVIEW_NOT_MODIFIED("Review Not Modified", 400),
    REVIEW_NOT_REMOVED("Review Not Removed", 400),
    REVIEW_NOT_FOUND("Review Not Found", 404),
    REVIEWER_MISMATCH("Reviewer Mismatch", 400),

    REVIEW_NOT_MATCHED("Review Not Matched", 404);

    private final ReviewTaskException reviewTaskException;

    ReviewExceptions(String msg, int code) {
        reviewTaskException = new ReviewTaskException(msg, code);
    }

    public ReviewTaskException get() {
        return reviewTaskException;
    }
}
