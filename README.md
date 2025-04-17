# 📝 Boardio
게시글과 댓글을 중심으로 자유롭게 소통하고, 알림과 검색, 신고 등 다양한 기능을 통해
<br>
보다 활발하고 안전한 **커뮤니티 활동을 지원하는 서비스**입니다.
<br>
관리자 페이지를 통해서는 공지사항과 배너를 손쉽게 운영하고, 회원과 게시글 및 댓글을 관리할 수 있습니다.

### 📝 목차
1. [사용 기술](##-🛠️-사용-기술)
2. [기능](##-🚀-기능)
3. [시스템 아키텍쳐](##-🧩-시스템-아키텍쳐)
4. [ERD 구조도](##-🗂️-ERD-구조도)
5. [문제해결 사례](#🔍-문제해결-사례)


## 🛠️ 사용 기술
- Backend: Spring Legacy, Spring Security, MyBatis
- Infra: AWS EC2, RDS(MySQL), S3, Redis
- Auth: Google OAuth 2.0, Email 인증
- Frontend: JSP, CkEditor, JavaScript
- 기타: WebSocket, ETag, SMTP

## 🚀 기능
  ### 공통
  - 회원가입(이메일 인증, Google 로그인)/탈퇴
  - 로그인(비밀번호 재설정 이메일)/로그아웃
  - 검색 로직(CkEditor 최적화 - 텍스트 파싱)
  
  ### 사용자 페이지
  - 새 댓글 알림(웹소켓)
  - 배너(Etag 캐시)
  - 게시글, 댓글 CRUD (AWS S3)
  - 공지사항 읽기 및 첨부파일 다운로드
  - 게시글 및 댓글 신고
  - 회원정보 수정
  
  ### 관리자
  - 방문자 카운트(Redis 캐시메모리)
  - 수동 및 자동 CkEditor 가비지 이미지 처리 로직
  - 공지사항 및 배너 CRUD
  - 게시글 및 댓글 차단 및 관리
  - 사용자 관리

## 🧩 시스템 아키텍쳐
![Boardio_system.png](https://github.com/nanadoo99/t1/blob/master/Boardio_system.png)


## 🗂️ ERD 구조도
![Boardio_ERD.png](https://github.com/nanadoo99/Boardio/blob/master/Boardio_ERD.png)


## 🔍 문제해결 사례
  ### 배경
  파일 업로드는 4가지에 대해 이루어짐
  
  1. 배너이미지
  2. 공지사항 첨부파일
  3. 사용자 게시글 내 이미지(CkEditor)
  4. 공지사항 게시글 내 이미지(CkEditor)
  
  업로드 유형에 맞게 2가지 경우의 수로 나누어 구현
  
  ![Boardio_문제해결사례_before.png](https://github.com/nanadoo99/t1/blob/master/Boardio_%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0%EC%82%AC%EB%A1%80_before.png)
  (CkEditor로 업로드된 이미지와 실제 저장되는 게시글 데이터의 불일치를 없애는 로직 구현)
  
  ### 문제 원인
  S3 추가 도입 >> 4가지 경우의 수 발생  
  저장소 위치(S3 vs 서버)와 업로드 유형(CkEditor vs 일반)

  ### 해결안
  - 업로드 유형: FileUploader과 이를 상속하는 인터페이스 정의.
  - 저장소 위치: 추상 및 구체 유틸리티 클래스로 외부에서 주입.

  ![Boardio_문제해결사례_after.png](https://github.com/nanadoo99/t1/blob/master/Boardio_%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0%EC%82%AC%EB%A1%80_after.png)
  
  #### 1. Interface 계층
  - **FileUploader**: 기본 업로드/삭제 기능 제공
  - **MultiFileUploader**: 다중 업로드/삭제 처리
  - **BaseFileUploader**:  CkEditorFileUploader와의 구분을 위한 마커 인터페이스
  - **CkEditorFileUploader**: CKEditor 관련 확장 기능 제공 (만료 파일 정리, 세트 삭제 등)
  
  #### 2. 추상 클래스
  - **AbstractLocalBaseFileUploader**: 로컬 업로더의 공통 동작 추상화
  - **AbstractS3BaseFileUploader**: S3 업로더 공통 동작 추상화(추상 AwsS3Utils 이용)
  - **AbstractS3CkEditorFileUploader**: CKEditor 전용 S3 업로더 공통 기능(추상 AwsS3Utils 이용)
  
  #### 3. 구현 클래스 (업로드 대상 및 방식별)
  - **AbstractLocalBaseFileUploaderBanner**
  - **S3BaseMultiFileUploaderAnnounce**(AwsS3Utils 구현체 이용)
  - **S3CkEditorFileUploaderAnnounce**(AwsS3Utils 구현체 이용)
  - **S3CkEditorFileUploaderPost**(AwsS3Utils 구현체 이용)
  
  #### 4. 추상 및 구체 유틸리티 클래스
  - **AwsS3Utils(추상)**: S3 관련 파일 처리 전반 담당
      - **AwsS3UtilsAnnounce**, **AwsS3UtilsPost**: 업로드 대상별 설정 구분
  - **FileUtils**: 파일명, 확장자 추출 및 용량 검사 등
  - **LocalFileUtils**: 로컬 경로 유효성 검사 등
  - **CkEditorUtils**: CkEditor 전용 파일 세트 추출 등
  
