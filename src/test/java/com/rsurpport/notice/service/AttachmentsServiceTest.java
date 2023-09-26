package com.rsurpport.notice.service;

import com.rsurpport.notice.common.enums.ErrorCode;
import com.rsurpport.notice.common.exception.CommonException;
import com.rsurpport.notice.entity.Attachments;
import com.rsurpport.notice.repository.AttachmentsRepository;
import com.rsurpport.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 첨부파일 단위테스트
 * @author songhyunsuk
 */
public class AttachmentsServiceTest {

    @Mock
    private AttachmentsRepository attachmentsRepository;

    @InjectMocks
    private AttachmentsService attachmentsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("첨부파일 등록")
    @Test
    public void testAddAttachments() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getSize()).thenReturn(100L);

        attachmentsService.addAttachments(1L, Arrays.asList(mockFile));

        verify(attachmentsRepository, times(1)).save(any(Attachments.class));
    }

    @DisplayName("첨부파일 수정")
    @Test
    public void testUpdateAttachments() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("updated_test.txt");
        when(mockFile.getSize()).thenReturn(100L);

        attachmentsService.updateAttachments(1L, Arrays.asList(mockFile));

        verify(attachmentsRepository, times(1)).deleteAttachmentsByNoticeId(1L);
        verify(attachmentsRepository, times(1)).save(any(Attachments.class));
    }

    @DisplayName("첨부파일 삭제")
    @Test
    public void testDeleteAttachmentsByNoticeId() {
        attachmentsService.deleteAttachmentsByNoticeId(1L);
        verify(attachmentsRepository, times(1)).deleteAttachmentsByNoticeId(1L);
    }

}
