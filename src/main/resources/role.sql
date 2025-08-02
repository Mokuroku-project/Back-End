CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE   -- 예: 'USER', 'ADMIN', 'MANAGER'
);

-- id	    BIGINT	    역할 ID (PK)
-- name	    VARCHAR(50)	역할 이름 (유일)