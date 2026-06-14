package com.zxw.aichat.base.utils;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public final class StartupTipUtils {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private StartupTipUtils() {
    }

    public static void printBeforeStart() {
        System.out.println(String.join(LINE_SEPARATOR,
                "",
                "  /\\_/\\\\",
                " ( o.o )   chat-base is waking up...",
                "  > ^ <",
                ""));
    }

    public static void printStarted(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name", "chat-base");
        String port = environment.getProperty("server.port", "8081");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String localUrl = "http://localhost:" + port + normalizeContextPath(contextPath);

        System.out.println(String.join(LINE_SEPARATOR,
                "",
                "  /\\_/\\\\",
                " ( -.- )   " + applicationName + " started successfully.",
                "  > ^ <    Local: " + localUrl,
                ""));
    }

    private static String normalizeContextPath(String contextPath) {
        if (!StringUtils.hasText(contextPath) || "/".equals(contextPath)) {
            return "";
        }
        return contextPath.startsWith("/") ? contextPath : "/" + contextPath;
    }
}
