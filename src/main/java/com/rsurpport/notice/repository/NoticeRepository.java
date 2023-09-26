package com.rsurpport.notice.repository;

import com.rsurpport.notice.dto.NoticeDto;
import com.rsurpport.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 공지 Repository
 * @author songhyunsuk
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{
    @Transactional
    @Modifying
    @Query("update Notice n set n.title = :title, n.content = :content, n.startDate = :startDate, n.endDate = :endDate where n.id = :id")
    int updateNoticeById(String title, String content, LocalDateTime startDate, LocalDateTime endDate, @NonNull Long id);
    @Transactional
    @Modifying
    @Query(value = "update notice set view_count = view_count + 1 where id = :id", nativeQuery = true)
    void incrementViewCount(Long id);

}
