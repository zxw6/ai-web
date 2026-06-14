package com.zxw.aichat.base;

import com.zxw.aichat.base.utils.StartupTipUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.zxw.aichat.base.mapper")
@SpringBootApplication
public class ChatBaseApplication {

    public static void main(String[] args) {
        StartupTipUtils.printBeforeStart();
        ConfigurableApplicationContext context = SpringApplication.run(ChatBaseApplication.class, args);
        StartupTipUtils.printStarted(context.getEnvironment());
    }
}
