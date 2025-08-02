CREATE TABLE member (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL UNIQUE,
                        profile_image_id BIGINT, -- file 테이블 참조
                        social_login_check CHAR(1) DEFAULT '0', -- '1' = 소셜 로그인, '0' = 일반 로그인
                        reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        withdrawal_date DATETIME,
                        status CHAR(1) DEFAULT '1', -- '1' = 사용 가능, '0' = 정지/탈퇴
                        CONSTRAINT fk_profile_image FOREIGN KEY (profile_image_id) REFERENCES file(id)
);

-- id	                BIGINT	        기본 키
-- email	            VARCHAR(255)	로그인용 이메일 (중복 불가)
-- password	            VARCHAR(255)    암호화된 비밀번호
-- nickname	            VARCHAR(50)	    닉네임 (중복 불가)
-- profile_image_id	    BIGINT	        프로필 이미지 ID (file 테이블 외래 키)
-- social_login_check	CHAR(1) 	    소셜 로그인 여부 ('1': 소셜, '0': 일반)
-- reg_date	            DATETIME	    가입일
-- withdrawal_date	    DATETIME	    탈퇴일
-- status	            CHAR(1)	        계정 상태 ('1': 정상, '0': 정지 또는 탈퇴)