package com.zxw.aichat.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.aichat.base.entity.AiCallLog;
import java.util.List;

public interface AiCallLogService extends IService<AiCallLog> {

    List<AiCallLog> listCallLogs(Long userId, Long sessionId);

    Long createCallLog(AiCallLog callLog);
}
