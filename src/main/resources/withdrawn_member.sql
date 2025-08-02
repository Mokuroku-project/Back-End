CREATE TABLE withdrawn_member (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  email VARCHAR(255) NOT NULL,
                                  nickname VARCHAR(50),
                                  withdrawal_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  reason TEXT,
                                  backup_data JSON,  -- 선택: 개인정보 일부를 암호화 또는 요약 저장
                                  deleted BOOLEAN DEFAULT FALSE
);

-- 이 테이블은 탈퇴한 회원의 일부 정보를 백업하거나 감사용으로 보존하기 위한 용도로 사용됩니다.
-- id	            BIGINT	        기본 키
-- email	        VARCHAR(255)	탈퇴한 사용자의 이메일
-- nickname	        VARCHAR(50) 	닉네임 (선택적으로 저장)
-- withdrawal_date	DATETIME	    탈퇴 일시
-- reason	        TEXT	        탈퇴 사유 (자유 입력)
-- backup_data	    JSON	        암호화된 백업 정보 (예: 활동 내역, 가입일 등)
-- deleted	        BOOLEAN	        완전 삭제 여부 (GDPR 대응 등)

-- ✅ 활용 목적
-- 정책에 따른 일정 기간 보존 (예: 6개월 후 완전 삭제)
-- 이탈 분석, 불법 재가입 방지, 데이터 감사 용도
-- backup_data 필드는 선택적으로 암호화된 개인정보 요약 또는 로그를 담을 수 있음