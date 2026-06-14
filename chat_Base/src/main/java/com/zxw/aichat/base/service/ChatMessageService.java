package com.zxw.aichat.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.aichat.base.entity.ChatMessage;
import java.util.List;

public interface ChatMessageService extends IService<ChatMessage> {

    List<ChatMessage> listMessages(Long sessionId);

    Long createMessage(ChatMessage message);

    boolean updateMessage(Long id, ChatMessage message);

    boolean deleteMessage(Long id);
}
