package com.zxw.aichat.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.aichat.base.entity.ChatMessage;
import com.zxw.aichat.base.mapper.ChatMessageMapper;
import com.zxw.aichat.base.service.ChatMessageService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    @Override
    public List<ChatMessage> listMessages(Long sessionId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<ChatMessage>()
                .eq(sessionId != null, ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreatedTime);
        return list(wrapper);
    }

    @Override
    public Long createMessage(ChatMessage message) {
        save(message);
        return message.getId();
    }

    @Override
    public boolean updateMessage(Long id, ChatMessage message) {
        message.setId(id);
        return updateById(message);
    }

    @Override
    public boolean deleteMessage(Long id) {
        return removeById(id);
    }
}
