CREATE TABLE social_account (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                member_id BIGINT NOT NULL,
                                provider VARCHAR(50) NOT NULL,         -- 예: 'google', 'naver', 'kakao'
                                provider_id VARCHAR(255) NOT NULL,     -- 소셜 제공자의 사용자 고유 ID
                                linked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_social_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- id	        BIGINT	        기본 키
-- member_id	BIGINT	        연결된 사용자 ID (member 테이블 외래 키)
-- provider	    VARCHAR(50)	    소셜 로그인 제공자 (예: google, naver, kakao)
-- provider_id	VARCHAR(255)	해당 소셜 플랫폼의 유저 고유 식별자
-- linked_at	DATETIME	    계정이 처음 연결된 시각

-- ✅ 참고 사항
-- provider_id는 Google의 경우 sub 값, Naver는 id 값 등 소셜별로 다릅니다.
-- 하나의 사용자(member_id)가 여러 소셜 계정을 연결할 수 있도록 1:N 관계로 설계되어 있습니다.