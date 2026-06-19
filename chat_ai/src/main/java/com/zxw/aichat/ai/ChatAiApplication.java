package com.zxw.aichat.ai;

import com.zxw.aichat.ai.utils.StartupTipUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.zxw.aichat.ai.mapper")
@SpringBootApplication
public class ChatAiApplication {

    public static void main(String[] args) {
        StartupTipUtils.printBeforeStart();
        ConfigurableApplicationContext context = SpringApplication.run(ChatAiApplication.class, args);
        StartupTipUtils.printStarted(context.getEnvironment());
    }
}
