package com.rsurpport.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * NOTICE
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Attachments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileOrgName;
    private String fileUrl;
    private Long noticeId;

    public Attachments() {
    }
}
