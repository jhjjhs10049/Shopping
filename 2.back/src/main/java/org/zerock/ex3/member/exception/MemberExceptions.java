package org.zerock.ex3.member.exception;

public enum MemberExceptions {

    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 409),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", 409),  // 이메일 중복 예외 추가
    INVALID("INVALID", 400),

    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private MemberTaskException memberTaskException;

    MemberExceptions(String msg, int code){
        memberTaskException = new MemberTaskException(msg,code);
    }

    public MemberTaskException get(){
        return memberTaskException;
    }
}
