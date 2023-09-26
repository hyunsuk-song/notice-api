package com.rsurpport.notice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러코드 정의
 * @author songhyunsuk
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    API_ERR_NOTICE_WRITER_VALIDATION("4001", "작성자가 다릅니다."),
    API_ERR_NOTICE_DATE_VALIDATION("4002", "시작일보다 종료일의 날짜가 더 빠릅니다."),
    API_ERR_NOTICE_ID_VALIDATION( "4003", "id가 없습니다."),
    API_ERR_NOTICE_NOT_EXISTS( "4004", "존재하지 않는 공지사항 입니다."),

    API_ERR_NOTICE_NULL_PARAMS( "4005", "필수 parameter값이 없습니다."),

    API_ERR_FILE( "4010", "파일 업로드 중 오류가 발생하였습니다."),
    API_ERR_FILE_DOWNLOAD( "4011", "존재 하지 않는 파일입니다."),
    ;

    private final String code;
    private final String message;
}
