CREATE TABLE file (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      file_name VARCHAR(255) NOT NULL,         -- 원본 파일명
                      file_path VARCHAR(500) NOT NULL,         -- 서버 또는 S3 저장 경로
                      uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      is_deleted BOOLEAN DEFAULT FALSE         -- 논리적 삭제 여부
);

-- 이 테이블은 프로필 이미지, 첨부파일, 썸네일 등 다양한 파일 정보를 저장하는 데 사용할 수 있습니다.
-- id	        BIGINT	파일 ID (기본 키)
-- file_name	VARCHAR(255)	업로드 당시의 원본 파일명
-- file_path	VARCHAR(500)	파일이 저장된 경로 (예: /images/profile/abc.jpg)
-- uploaded_at	DATETIME	업로드 일시
-- is_deleted	BOOLEAN	삭제 여부 (논리 삭제용 필드)

-- ✅ 사용 예
-- member.profile_image_id 외래 키로 참조하여 프로필 이미지 연결
-- 추후 파일 다운로드, 삭제, 복구 기능 등에 활용

