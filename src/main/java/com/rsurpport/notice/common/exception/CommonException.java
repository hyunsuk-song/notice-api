package com.rsurpport.notice.common.exception;

import com.rsurpport.notice.common.enums.ErrorCode;
import lombok.Getter;

/**
 *
 * @author songhyunsuk
 */
@Getter
public class CommonException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String message;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CommonException(ErrorCode errorCode, Object message) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = String.valueOf(message);
    }

    public static CommonException of(ErrorCode errorCode){
        return new CommonException(errorCode);
    }
}