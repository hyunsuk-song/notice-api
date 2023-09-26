package com.rsurpport.notice.service;

import com.rsurpport.notice.common.enums.ErrorCode;
import com.rsurpport.notice.common.exception.CommonException;
import com.rsurpport.notice.entity.Attachments;
import com.rsurpport.notice.entity.Notice;
import com.rsurpport.notice.repository.AttachmentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 공지 첨부파일 Service
 * {@code @create_date}
 * @author songhyunsuk
 */
@Slf4j
@Service( value = "attachmentsService")
public class AttachmentsService {

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;


    /**
     * 파일 추가
     * @param id
     * @param fileList
     * @throws Exception
     */
    @Transactional
    public void addAttachments(Long id, List<MultipartFile> fileList){
        this.uploadFiles(id, fileList);
    }


    /**
     * 파일 수정(삭제&추가)
     * @param id
     * @param fileList
     */
    @Transactional
    public void updateAttachments(Long id, List<MultipartFile> fileList) {
        this.deleteAttachmentsByNoticeId(id);
        this.uploadFiles(id, fileList);
    }


    /**
     * 파일 삭제
     * @param noticeId
     */
    @Transactional
    public void deleteAttachmentsByNoticeId(Long noticeId) {

        List<Attachments> attachmentsList = attachmentsRepository.findAttachmentsByNoticeId(noticeId);

        attachmentsList.forEach(attachments -> {
            this.removeFile(attachments.getFileUrl());
        });

        attachmentsRepository.deleteAttachmentsByNoticeId(noticeId);
    }

    /**
     * 파일 업로드
     * @param noticeId
     * @param files
     */
    public void uploadFiles(Long noticeId, List<MultipartFile> files) {
        // 파일 업로드 경로 생성
        // 공지 마다 폴더를 따로 생성
        String noticeUploadDir = uploadDir + File.separator + "notice_" + noticeId;
        File noticeDir = new File(noticeUploadDir);
        if (!noticeDir.exists()) {
            noticeDir.mkdirs();
        }
        noticeDir.canWrite();
        // 파일 저장
        for (MultipartFile multipartFile : files) {
            String originFileName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originFileName;
            try {
                String fileUrl = Paths.get(noticeUploadDir + File.separator + fileName).toString();
                File file = new File(fileUrl);
                multipartFile.transferTo(file);
                Notice notice = new Notice();
                notice.setId(noticeId);
                Attachments attachments = Attachments.builder()
                        .noticeId(noticeId)
                        .fileOrgName(originFileName)
                        .fileName(fileName)
                        .fileUrl(fileUrl) // 경로를 문자열로 설정
                        .build();


                log.debug("attachments : {}",attachments);

                // 파일 정보를 DB에 저장
                attachmentsRepository.save(attachments);

            } catch (IOException e) {
                e.printStackTrace();
                throw new CommonException(ErrorCode.API_ERR_FILE, e.getMessage());
            }
        }
    }
    public void removeFile(String path) {
        File file = new File(path);
        try {
            log.debug("path {}",path);
            file.delete();
        } catch (Exception e) {
            throw new CommonException(ErrorCode.API_ERR_NOTICE_ID_VALIDATION);
        }
    }

}
