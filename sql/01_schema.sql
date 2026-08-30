-- 簡易日報アプリ用スキーマ
-- MySQL 8.0 Community想定

CREATE DATABASE IF NOT EXISTS nippou_db DEFAULT CHARACTER SET utf8mb4;

USE nippou_db;

DROP TABLE IF EXISTS reports;

CREATE TABLE reports (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    reporter_name VARCHAR(50)  NOT NULL,
    report_date   DATE         NOT NULL,
    work_content  TEXT         NOT NULL,
    remarks       VARCHAR(500),
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME     NOT NULL
) DEFAULT CHARACTER SET utf8mb4;

-- アプリ接続用ユーザー(ローカル環境用。本番運用の権限設計ではない点に注意)
CREATE USER IF NOT EXISTS 'nippou_user'@'localhost' IDENTIFIED BY 'nippou_pass';
GRANT ALL PRIVILEGES ON nippou_db.* TO 'nippou_user'@'localhost';
FLUSH PRIVILEGES;
