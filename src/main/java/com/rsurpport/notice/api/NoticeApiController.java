package com.rsurpport.notice.api;


import com.rsurpport.notice.common.enums.ErrorCode;
import com.rsurpport.notice.common.exception.CommonException;
import com.rsurpport.notice.dto.NoticeDto;
import com.rsurpport.notice.entity.Notice;
import com.rsurpport.notice.service.AttachmentsService;
import com.rsurpport.notice.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 공지사항 API
 * @author songhyunsuk
 */
@RestController
@RequestMapping("/api")
public class NoticeApiController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeApiController.class);

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private AttachmentsService attachmentsService;


    /**
     * 공지 목록
     * @return
     */
    @GetMapping("/notices")
    public List<Notice> getNoticeAll(){

        return noticeService.getNoticeAll();
    }

    /**
     * 공지 조회
     * @param id
     * @return
     */
    @GetMapping("/notices/{id}")
    public Notice getNotice(@PathVariable("id") long id){

        return noticeService.getNoticeViewCountUp(id);
    }

    /**
     * 공지 등록
     * @param noticeDto
     * @param multipartFileList
     */
    @PostMapping(value = "/notices",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void registerNotice(@RequestPart NoticeDto noticeDto,
                               @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFileList,
                               @RequestHeader("writer") String writer) {

        logger.debug("registerNotice start");

        // 시작일과 종료일 체크
        if(noticeDto.getStartDate().isAfter(noticeDto.getEndDate())){
            throw new CommonException(ErrorCode.API_ERR_NOTICE_DATE_VALIDATION);
        }

        // 작성자는 헤더값에서 가져온다.
        noticeDto.setWriter(writer);
        Notice notice =  noticeService.registerNotice(noticeDto);

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
           attachmentsService.addAttachments(notice.getId(), multipartFileList);
        }
    }

    /**
     * 공지 수정
     * @param id
     * @param noticeDto
     * @param multipartFileList
     */
    @PutMapping("/notices/{id}")
    public void updateNotice(@PathVariable("id") long id,
                             @RequestPart NoticeDto noticeDto,
                             @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFileList,
                             @RequestHeader("writer") String writer) {

        logger.debug("updateNotice start , RequestHeader writer : " + writer);

        // 시작일과 종료일 체크
        if(noticeDto.getStartDate().isAfter(noticeDto.getEndDate())){
            throw new CommonException(ErrorCode.API_ERR_NOTICE_DATE_VALIDATION);
        }

        // 작성자는 헤더값에서 가져온다.
        noticeDto.setWriter(writer);
        noticeDto.setId(id);
        noticeService.updateNotice(noticeDto);

        // 파일이 있을때만
        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            attachmentsService.updateAttachments(id, multipartFileList);
        }
    }

    /**
     * 공지 삭제
     * @param id
     */
    @DeleteMapping("/notices/{id}")
    public void removeNotice(@PathVariable("id") long id,
                             @RequestHeader("writer") String writer) {

        logger.debug("removeNotice start , RequestHeader writer : " + writer);
        attachmentsService.deleteAttachmentsByNoticeId(id);
        noticeService.deleteNotice(id, writer);
    }
}
