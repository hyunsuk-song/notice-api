package com.rsurpport.notice.repository;

import com.rsurpport.notice.entity.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 공지 첨부 파일 Repository
 * @author songhyunsuk
 */
@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long>{
    List<Attachments> findAttachmentsByNoticeId(Long id);
    void deleteAttachmentsByNoticeId(Long id);


}
