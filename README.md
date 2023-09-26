# 알서포트_과제
- 공지를 등록,조회,수정,삭제하는 API 개발을 과제로 수행하였습니다. 

## 개발
- 송현숙
### 기술 스택 및 개발 환경
- IntelliJ
- Java JDK 18
- Spring Boot 3.1.1
- Hibernate
- Junit 5
- Lombok
- Gradle
- H2

## API 기능
- 공지 목록
- 공지 상세 조회
- 공지 등록
- 공지 수정
- 공지 삭제 

### API 기능분석
- 공지 목록
  - 시작일과 종료일이 있다는것은 이 오늘 날짜가 이 사이에 있어야 노출시킨다.
    - 명확하지 않은 요구사항이므로 먼저 개발하지 않고 이슈업만 한다.
- 공지 상세 조회
  - 시작일과 종료일사이에 오늘날짜가 있어야 한다.
  - 조회수를 증가시킨다.
- 공지 등록
  - 첨부파일을 여러개 등록할수 있다.
  - 공지 시작일은 종료일보다 클수 없다.
- 공지 수정
  - 작성자가 기존작성자와 같아야 수정할 수 있다.
    - 아마도 로그인 id라고 생각하면, 넘어오는 로그인id와 기존작성자를 비교한다. 
    - 요구사항에 들어 있지 않지만 기능상 필요하다고 생각되므로 개발
  - 첨부파일을 여러개 등록할수 있다.
  - 첨부파일을 삭제 후 새롭게 등록한다.
  - 공지 시작일은 종료일보다 클 수 없다.
- 공지 삭제
  - 작성자가 기존작성자와 같아야 삭제할 수 있다.
    - 아마도 로그인 id라고 생각하면, 넘어오는 로그인id와 기존작성자를 비교한다.
    - 요구사항에 들어 있지 않지만 기능상 필요하다고 생각되므로 개발
  - 첨부파일을 먼저 삭제후(파일 및 table row) 공지를 삭제한다.

### 공지사항 API 명세서

| 기능 | HTTP 메소드 | 엔드포인트 | 설명 | prams                                                                                                                                                                                                                         |
| --- | --- | --- | --- |-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| 공지 목록 조회 | GET | /api/notices | 모든 공지사항의 목록을 조회합니다. |                                                                                                                                                                                                                               |
| 공지사항 조회 | GET | /api/notices/{id} | 특정 공지사항을 조회하며,<br/> 조회 시 조회수가 증가합니다. | id: 조회할 공지사항의 id                                                                                                                                                                                                              |
| 공지사항 등록 | PUT | /api/notices | 새로운 공지사항을 등록합니다. <br/> 첨부파일을 포함할 수 있습니다. | {<br/> "title": "성공하는 방법34",<br/>"content": "공지사항을 알려드립니다.33",<br/>"startDate": "2023-09-20 13:30:00",<br/>"endDate": "2023-09-20 13:30:00"<br/>},<br/>  multipartFile: 첨부파일 (옵션)                        |
| 공지사항 수정 | PUT | /api/notices/{id} | 특정 공지사항을 수정합니다. <br/> 첨부파일 업데이트도 가능합니다. | id: 수정할 공지사항의 id, <br/> {<br/> "title": "성공하는 방법34",<br/>"content": "공지사항을 알려드립니다.33",<br/>"startDate": "2023-09-20 13:30:00",<br/>"endDate": "2023-09-20 13:30:00"<br/>},<br/> multipartFile: 첨부파일 (옵션) |
| 공지사항 삭제 | DELETE | /api/notices/{id} | 특정 공지사항을 삭제합니다. | id: 삭제할 공지사항의 id                                                                                                                                                                                                              |

### 공지사항 에러 코드 명세
| Name                              | Code | Description                   |
|-----------------------------------|------|-------------------------------|
| API_ERR_NOTICE_WRITER_VALIDATION  | 4001 | 작성자가 다릅니다.                 |
| API_ERR_NOTICE_DATE_VALIDATION    | 4002 | 시작일보다 종료일의 날짜가 더 빠릅니다. |
| API_ERR_NOTICE_ID_VALIDATION      | 4003 | id가 없습니다.                   |
| API_ERR_NOTICE_NOT_EXISTS         | 4004 | 존재하지 않는 공지사항 입니다.        |
| API_ERR_NOTICE_NULL_PARAMS        | 4005 | 필수 parameter값이 없습니다.       |
| API_ERR_FILE                      | 4010 | 파일 업로드 중 오류가 발생하였습니다.   |


## 테스트 환경 및 고려사항
- 파일 업로드의 경우 비동기로 먼저 파일을 업로드 하고 URL을 가져와서 다운로드시 사용하는 방식으로 진행하였습니다.
- 서버 실행은 NoticeServiceApplication을 실행 시키면 됩니다.
  - 이때, h2 DB에 필요한 테이블이 만들어집니다.(/resources/db/schema-h2.sql 을 참조하여 만들어집니다.)
  - h2-console URL :  http://localhost:8080/h2-console 
  - 처음 들어갔을 경우, Generic H2 (Embedded) 선택, JDBC URL = jdbc:h2:~/notice 으로 합니다. 
  - 계정은 그대로 connect 하시면 됩니다. (비번설정은 하지 않았습니다.) 
- 기능 단위 테스트는 서비스 기준으로 작성하였습니다.
  - NoticeServiceTest
  - AttachmentsServiceTest 
- 통합테스트로는 .http를 사용하였습니다.
  - /http 아래 notice.http 파일을 두고 오른쪽 마우스 클릭 후 실행을 시킵니다. 
  - 실행 전, 서버가 run 상태여야 실행 됩니다.

** 그외에 포스트맨을 통해서 공지등록 및 파일첨부등의 기능들을 테스트하였습니다.!

![img_1.png](file%2Ftest%2Fimg_1.png)
