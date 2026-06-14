CREATE DATABASE IF NOT EXISTS `ai_chat`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `ai_chat`;

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `username` VARCHAR(64) NOT NULL COMMENT 'Login username',
    `password_hash` VARCHAR(255) NOT NULL COMMENT 'Password hash',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT 'Display name',
    `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT 'Avatar URL',
    `email` VARCHAR(128) DEFAULT NULL COMMENT 'Email',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 1 enabled, 0 disabled',
    `last_login_time` DATETIME DEFAULT NULL COMMENT 'Last login time',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logic delete: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System user';

CREATE TABLE IF NOT EXISTS `chat_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `title` VARCHAR(128) NOT NULL DEFAULT 'New Chat' COMMENT 'Session title',
    `model_code` VARCHAR(64) NOT NULL COMMENT 'Model code',
    `system_prompt` TEXT DEFAULT NULL COMMENT 'System prompt',
    `message_count` INT NOT NULL DEFAULT 0 COMMENT 'Message count',
    `last_message_time` DATETIME DEFAULT NULL COMMENT 'Last message time',
    `pinned` TINYINT NOT NULL DEFAULT 0 COMMENT 'Pinned: 0 no, 1 yes',
    `archived` TINYINT NOT NULL DEFAULT 0 COMMENT 'Archived: 0 no, 1 yes',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logic delete: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    KEY `idx_chat_session_user_updated` (`user_id`, `updated_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat session';

CREATE TABLE IF NOT EXISTS `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `session_id` BIGINT NOT NULL COMMENT 'Session ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `role` VARCHAR(16) NOT NULL COMMENT 'Role: user, assistant, system',
    `content` LONGTEXT NOT NULL COMMENT 'Message content',
    `model_code` VARCHAR(64) DEFAULT NULL COMMENT 'Model code',
    `response_id` VARCHAR(128) DEFAULT NULL COMMENT 'Provider response ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 1 success, 0 failed, 2 processing',
    `error_message` TEXT DEFAULT NULL COMMENT 'Error message',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    KEY `idx_chat_message_session_created` (`session_id`, `created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat message';

CREATE TABLE IF NOT EXISTS `ai_model` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `provider` VARCHAR(32) NOT NULL COMMENT 'Provider code',
    `model_code` VARCHAR(64) NOT NULL COMMENT 'Model code',
    `model_name` VARCHAR(128) NOT NULL COMMENT 'Model display name',
    `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT 'Enabled: 1 yes, 0 no',
    `default_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'Default model: 1 yes, 0 no',
    `sort` INT NOT NULL DEFAULT 0 COMMENT 'Sort order',
    `max_output_tokens` INT NOT NULL DEFAULT 2048 COMMENT 'Max output tokens',
    `temperature` DECIMAL(4,2) NOT NULL DEFAULT 0.70 COMMENT 'Temperature',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ai_model_model_code` (`model_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI model config';

CREATE TABLE IF NOT EXISTS `ai_call_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `session_id` BIGINT DEFAULT NULL COMMENT 'Session ID',
    `request_message_id` BIGINT DEFAULT NULL COMMENT 'Request message ID',
    `response_message_id` BIGINT DEFAULT NULL COMMENT 'Response message ID',
    `provider` VARCHAR(32) NOT NULL COMMENT 'Provider code',
    `model_code` VARCHAR(64) NOT NULL COMMENT 'Model code',
    `response_id` VARCHAR(128) DEFAULT NULL COMMENT 'Provider response ID',
    `prompt_tokens` INT NOT NULL DEFAULT 0 COMMENT 'Prompt tokens',
    `completion_tokens` INT NOT NULL DEFAULT 0 COMMENT 'Completion tokens',
    `total_tokens` INT NOT NULL DEFAULT 0 COMMENT 'Total tokens',
    `latency_ms` BIGINT NOT NULL DEFAULT 0 COMMENT 'Latency in milliseconds',
    `stream` TINYINT NOT NULL DEFAULT 1 COMMENT 'Stream response: 1 yes, 0 no',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 1 success, 0 failed',
    `error_code` VARCHAR(64) DEFAULT NULL COMMENT 'Error code',
    `error_message` TEXT DEFAULT NULL COMMENT 'Error message',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    PRIMARY KEY (`id`),
    KEY `idx_ai_call_log_user_created` (`user_id`, `created_time`),
    KEY `idx_ai_call_log_session_created` (`session_id`, `created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI call log';
