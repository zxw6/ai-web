package com.zxw.aichat.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.aichat.base.entity.ChatSession;
import java.util.List;

public interface ChatSessionService extends IService<ChatSession> {

    List<ChatSession> listSessions(Long userId);

    Long createSession(ChatSession session);

    boolean updateSession(Long id, ChatSession session);

    boolean deleteSession(Long id);
}
