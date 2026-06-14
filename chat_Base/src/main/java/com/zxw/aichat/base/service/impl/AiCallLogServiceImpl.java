package com.zxw.aichat.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.aichat.base.entity.AiCallLog;
import com.zxw.aichat.base.mapper.AiCallLogMapper;
import com.zxw.aichat.base.service.AiCallLogService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AiCallLogServiceImpl extends ServiceImpl<AiCallLogMapper, AiCallLog> implements AiCallLogService {

    @Override
    public List<AiCallLog> listCallLogs(Long userId, Long sessionId) {
        LambdaQueryWrapper<AiCallLog> wrapper = new LambdaQueryWrapper<AiCallLog>()
                .eq(userId != null, AiCallLog::getUserId, userId)
                .eq(sessionId != null, AiCallLog::getSessionId, sessionId)
                .orderByDesc(AiCallLog::getCreatedTime);
        return list(wrapper);
    }

    @Override
    public Long createCallLog(AiCallLog callLog) {
        save(callLog);
        return callLog.getId();
    }
}
