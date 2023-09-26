DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS notice;

CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    start_date DATETIME,
    end_date DATETIME,
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    view_count INT DEFAULT 0,
    writer VARCHAR(255)
);

CREATE TABLE attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notice_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_org_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (notice_id) REFERENCES notice(id)
);

