CREATE TABLE login_history (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               member_id BIGINT NOT NULL,
                               login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                               ip_address VARCHAR(45),
                               user_agent TEXT,
                               CONSTRAINT fk_login_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- id	        BIGINT	    기본 키
-- member_id	BIGINT	    로그인한 사용자 ID (member 테이블 외래 키)
-- login_time	DATETIME	로그인 시각 (기본: 현재 시간)
-- ip_address	VARCHAR(45)	로그인 시 사용한 IP 주소 (IPv4/IPv6 모두 지원)
-- user_agent	TEXT	    브라우저 및 디바이스 정보 (User-Agent 문자열)

-- ✅ 활용 예
-- 관리자 페이지에서 로그인 기록 확인
-- 이상 로그인 탐지 (다른 IP, 국가 등)
-- 보안 로그 감사 기록
