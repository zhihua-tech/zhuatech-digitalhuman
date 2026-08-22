-- Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/
CREATE DATABASE IF NOT EXISTS zhuatech_digitalhuman DEFAULT CHARACTER SET utf8mb4;
USE zhuatech_digitalhuman;

CREATE TABLE presentation_project (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_name VARCHAR(120) NOT NULL,
  script_text TEXT NOT NULL,
  avatar_id VARCHAR(80) NOT NULL,
  voice_id VARCHAR(80) NOT NULL,
  aspect_ratio VARCHAR(20) NOT NULL,
  authorization_status VARCHAR(30) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE presentation_job (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  provider_code VARCHAR(60) NOT NULL,
  provider_job_id VARCHAR(120),
  job_status VARCHAR(30) NOT NULL,
  disclosure_enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_presentation_job(project_id, job_status)
);
