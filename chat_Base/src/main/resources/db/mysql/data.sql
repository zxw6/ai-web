USE `chatai`;

INSERT INTO `sys_user` (
    `id`, `username`, `password_hash`, `nickname`, `status`
) VALUES (
    1, 'admin', '$2a$10$example.change.this.password.hash.before.production', 'Admin', 1
) ON DUPLICATE KEY UPDATE
    `nickname` = VALUES(`nickname`),
    `status` = VALUES(`status`);

INSERT INTO `ai_model` (
    `id`, `provider`, `model_code`, `model_name`, `enabled`, `default_flag`, `sort`, `max_output_tokens`, `temperature`
) VALUES (
    1, 'openai', 'gpt-4o-mini', 'GPT-4o mini', 1, 1, 1, 2048, 0.70
) ON DUPLICATE KEY UPDATE
    `model_name` = VALUES(`model_name`),
    `enabled` = VALUES(`enabled`),
    `default_flag` = VALUES(`default_flag`),
    `sort` = VALUES(`sort`);

INSERT INTO `chat_session` (
    `id`, `user_id`, `title`, `model_code`, `message_count`, `last_message_time`
) VALUES (
    1, 1, 'Demo Chat', 'gpt-4o-mini', 0, NOW()
) ON DUPLICATE KEY UPDATE
    `title` = VALUES(`title`),
    `model_code` = VALUES(`model_code`);
