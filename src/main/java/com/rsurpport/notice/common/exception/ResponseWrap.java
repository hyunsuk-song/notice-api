package com.rsurpport.notice.common.exception;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * @author songhyunsuk
 */
@Getter
public class ResponseWrap {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    private int status;
    private String message;

    public ResponseWrap(Object data) {
        status = 200;
        this.data = data;
        message = "Success";
    }

    public ResponseWrap(CommonException commonException) {
        status = 400;
        code = commonException.getErrorCode().getCode();
        message = commonException.getMessage();
    }

    public ResponseWrap(Exception exception) {
        this.status = 500;
        message = "오류가 발생하였습니다. 관리자에게 문의하세요.";
    }
}
