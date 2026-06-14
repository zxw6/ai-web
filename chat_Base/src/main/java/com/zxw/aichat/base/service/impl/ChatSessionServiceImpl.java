package com.zxw.aichat.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.aichat.base.entity.ChatSession;
import com.zxw.aichat.base.mapper.ChatSessionMapper;
import com.zxw.aichat.base.service.ChatSessionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {

    @Override
    public List<ChatSession> listSessions(Long userId) {
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<ChatSession>()
                .eq(userId != null, ChatSession::getUserId, userId)
                .orderByDesc(ChatSession::getPinned)
                .orderByDesc(ChatSession::getUpdatedTime);
        return list(wrapper);
    }

    @Override
    public Long createSession(ChatSession session) {
        save(session);
        return session.getId();
    }

    @Override
    public boolean updateSession(Long id, ChatSession session) {
        session.setId(id);
        return updateById(session);
    }

    @Override
    public boolean deleteSession(Long id) {
        return removeById(id);
    }
}
