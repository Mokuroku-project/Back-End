CREATE TABLE member_role (
                             member_id BIGINT NOT NULL,
                             role_id BIGINT NOT NULL,
                             PRIMARY KEY (member_id, role_id),
                             CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member(id),
                             CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id)
);

-- member_id	BIGINT	사용자 ID (member 외래 키)
-- role_id	    BIGINT	역할 ID (role 외래 키)

-- ✅ 이 구조의 장점
-- 유연한 권한 관리 가능 (예: 한 명이 USER, MODERATOR 둘 다 가질 수 있음)
-- member_role은 다대다(M:N) 관계를 표현하는 중간 테이블