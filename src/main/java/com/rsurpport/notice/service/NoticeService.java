package com.rsurpport.notice.service;

import com.rsurpport.notice.common.enums.ErrorCode;
import com.rsurpport.notice.common.exception.CommonException;
import com.rsurpport.notice.dto.NoticeDto;
import com.rsurpport.notice.entity.Notice;
import com.rsurpport.notice.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 공지 Service
 * @author songhyunsuk
 */
@Slf4j
@Service( value = "noticeService")
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private AttachmentsService attachmentsService;


    /**
     * 공지 목록
     * @return
     */
    @Cacheable(value = "noticeAll")
    public List<Notice> getNoticeAll() {

        return noticeRepository.findAll();
    }

    /**
     * 공지 조회
     * @param id
     * @return
     */
    public Optional<Notice> getNotice(Long id){
        return noticeRepository.findById(id);
    }

    /**
     * 공지 조회
     * (view count up)
     * @param id
     * @return
     */
    public Notice getNoticeViewCountUp(Long id){
        Notice notice = this.getNotice(id).orElseThrow(() -> new CommonException(ErrorCode.API_ERR_NOTICE_NOT_EXISTS));
        noticeRepository.incrementViewCount(notice.getId());
        noticeRepository.save(notice);
        return notice;
    }

    /**
     * 공지 등록
     * @param noticeDto
     */
    @Transactional
    public Notice registerNotice(NoticeDto noticeDto){

        Notice notice = Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .startDate(noticeDto.getStartDate())
                .endDate(noticeDto.getEndDate())
                .writer(noticeDto.getWriter()).build();

        log.info("notice : ", notice.toString());

        return noticeRepository.save(notice);
    }
    /**
     * 공지 수정
     * @param noticeDto
     */
    @Transactional
    public int updateNotice(NoticeDto noticeDto){
        log.info("noticeDot id = {}" , noticeDto.toString());
        Notice notice = this.getNotice(noticeDto.getId()).orElseThrow(() -> new CommonException(ErrorCode.API_ERR_NOTICE_NOT_EXISTS));

        // 기존 작성자와 수정하려는 작성자가 다를경우
        if(!notice.getWriter().equalsIgnoreCase(noticeDto.getWriter())){
            throw new CommonException(ErrorCode.API_ERR_NOTICE_WRITER_VALIDATION);
        }

        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setStartDate(noticeDto.getStartDate());
        notice.setEndDate(noticeDto.getEndDate());
       // notice.setWriter(noticeDto.getWriter());

        log.info("\n notice : {} \n", notice);

        return  noticeRepository.updateNoticeById(noticeDto.getTitle(), noticeDto.getContent(),noticeDto.getStartDate(),noticeDto.getEndDate(),noticeDto.getId());
    }

    /**
     * 공지 삭제
     * @param id
     */
    @Transactional
    public void deleteNotice(Long id, String writer){
        Notice notice = this.getNotice(id).orElseThrow(() -> new CommonException(ErrorCode.API_ERR_NOTICE_NOT_EXISTS));

        // 기존 작성자와 삭제하려는 작성자가 다를경우
        if(!notice.getWriter().equalsIgnoreCase(writer)){
            throw new CommonException(ErrorCode.API_ERR_NOTICE_WRITER_VALIDATION);
        }
        noticeRepository.delete(notice);
    }

}

