package com.rsurpport.notice.service;

import com.rsurpport.notice.common.enums.ErrorCode;
import com.rsurpport.notice.common.exception.CommonException;
import com.rsurpport.notice.dto.NoticeDto;
import com.rsurpport.notice.entity.Notice;
import com.rsurpport.notice.repository.NoticeRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * 공지사항 단위 테스트
 * @author songhyunsuk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("공지목록조회")
    @Test
    public void testGetNoticeAll() {
        noticeService.getNoticeAll();
        verify(noticeRepository, times(1)).findAll();
    }


    @DisplayName("공지조회")
    @Test
    public void testGetNotice() {
        noticeService.getNotice(1L);
        verify(noticeRepository, times(1)).findById(1L);
    }


    @DisplayName("공지등록")
    @Test
    public void testRegisterNotice() {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setTitle("Test Title");
        noticeDto.setContent("Test Content");
        noticeDto.setStartDate(LocalDate.of(2023,9,20).atStartOfDay());
        noticeDto.setEndDate(LocalDate.of(2023,9,30).atStartOfDay());
        noticeDto.setWriter("hyunsuk");
        noticeService.registerNotice(noticeDto);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }


    @DisplayName("공지수정_존재하지않는공지")
    @Test
    public void testUpdateNotice_notExists() {
        Long id = 1L;
        String title = "Test Update Title";
        String content = "Test Update Content";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        String writer = "hyunsuk";

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(id);
        noticeDto.setTitle(title);
        noticeDto.setContent(content);
        noticeDto.setStartDate(startDate);
        noticeDto.setEndDate(endDate);
        noticeDto.setWriter(writer);

        when(noticeRepository.findById(id)).thenReturn(Optional.empty());

        CommonException thrown = assertThrows(CommonException.class, () -> {
            noticeService.updateNotice(noticeDto);
        });

        assertEquals(ErrorCode.API_ERR_NOTICE_NOT_EXISTS, thrown.getErrorCode());
    }

    @DisplayName("공지수정_작성자다름")
    @Test
    public void testUpdateNotice_writerMismatch() {
        Long id = 1L;
        Notice notice = new Notice();
        notice.setWriter("hyunsuk");
        notice.setId(id);

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(id);
        noticeDto.setTitle("Test Update Title");
        noticeDto.setContent("Test Update Content");
        noticeDto.setStartDate(LocalDate.of(2023,9,20).atStartOfDay());
        noticeDto.setEndDate(LocalDate.of(2023,9,30).atStartOfDay());
        noticeDto.setWriter("diff_hyunsuk");

        when(noticeRepository.findById(noticeDto.getId())).thenReturn(Optional.of(notice));
        CommonException thrown = assertThrows(CommonException.class, () -> {
            noticeService.updateNotice(noticeDto);
        });

        assertEquals(ErrorCode.API_ERR_NOTICE_WRITER_VALIDATION, thrown.getErrorCode());
    }

    @DisplayName("공지수정_성공")
    @Test
    public void testUpdateNotice_success() {
        Long id = 1L;
        String title = "Test Update Title";
        String content = "Test Update Content";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        String writer = "hyunsuk";

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(id);
        noticeDto.setTitle("Test Update Title");
        noticeDto.setContent("Test Update Content");
        noticeDto.setStartDate(startDate);
        noticeDto.setEndDate(endDate);
        noticeDto.setWriter(writer);

        Notice notice = new Notice();
        notice.setId(id);
        notice.setWriter(writer);

        when(noticeRepository.findById(id)).thenReturn(Optional.of(notice));

        when(noticeService.updateNotice(noticeDto)).thenReturn(1);

        int intRow = noticeService.updateNotice(noticeDto);

        verify(noticeRepository, times(intRow)).updateNoticeById(title, content, startDate, endDate, id);
    }

    @DisplayName("공지삭제_존재하지않는공지")
    @Test
    public void testDeleteNotice_notExists() {
        Long id = 1L;
        String writer = "writer";

        when(noticeRepository.findById(id)).thenReturn(Optional.empty());

        CommonException thrown = assertThrows(CommonException.class, () -> {
            noticeService.deleteNotice(id, writer);
        });

        assertEquals(ErrorCode.API_ERR_NOTICE_NOT_EXISTS, thrown.getErrorCode());
    }

    @DisplayName("공지삭제_작성자다름")
    @Test
    public void testDeleteNotice_writerMismatch() {
        Long id = 1L;
        String writer = "diff_hyunsuk";

        Notice notice = new Notice();
        notice.setWriter("hyunsuk");

        when(noticeRepository.findById(id)).thenReturn(Optional.of(notice));

        CommonException thrown = assertThrows(CommonException.class, () -> {
            noticeService.deleteNotice(id, writer);
        });

        assertEquals(ErrorCode.API_ERR_NOTICE_WRITER_VALIDATION, thrown.getErrorCode());
    }


    @DisplayName("공지삭제_성공")
    @Test
    public void testDeleteNotice_success() {
        Long id = 1L;
        String writer = "hyunsuk";

        Notice notice = new Notice();
        notice.setWriter(writer);

        when(noticeRepository.findById(id)).thenReturn(Optional.of(notice));

        noticeService.deleteNotice(id, writer);

        verify(noticeRepository).delete(notice); // 공지사항 삭제 메서드가 호출됨
    }

}
