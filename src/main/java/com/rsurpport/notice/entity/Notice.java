package com.rsurpport.notice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NOTICE
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime startDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime endDate;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
    private LocalDateTime regDate;
    private int viewCount;
    @NotNull
    private String writer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeId", referencedColumnName = "id")
    private List<Attachments> attachmentsList = new ArrayList<>();


    //view count를 증가 시킨다.
    public int increaseViewCount(){
        this.viewCount += 1;
        return this.getViewCount();
    }

    public Notice() {

    }
}
