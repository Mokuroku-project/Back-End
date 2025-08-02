CREATE TABLE email_verification (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    token VARCHAR(255) NOT NULL,
                                    expiry_time DATETIME NOT NULL,
                                    verified BOOLEAN DEFAULT FALSE,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- id	        BIGINT	        기본 키
-- email	    VARCHAR(255)	인증 대상 이메일 (회원 이메일과 동일)
-- token	    VARCHAR(255)	인증용 토큰 문자열
-- expiry_time	DATETIME	    인증 토큰 만료 시간
-- verified	    BOOLEAN	        인증 완료 여부 (기본: FALSE)
-- created_at	DATETIME	    생성일시 (기본: 현재시간)

-- ✅ 참고 사항
-- email 필드는 member.email과 연동되지만, 외래 키로 강제하지 않는 경우가 많습니다. 이유는 인증 요청 시점에는 member 레코드가 아직 없을 수 있기 때문입니다.
-- 만약 회원 가입 후 인증하는 구조라면 외래 키로 연결해도 됩니다.
