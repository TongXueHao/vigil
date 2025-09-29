package org.htx.vigil.autoconfigure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.htx.vigil.logging.LogEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.LoggerContext;
import org.htx.vigil.logging.appender.LogbackAppender;
import org.slf4j.LoggerFactory;

import java.util.Iterator;


/**
 * Vigil 自动配置类
 * 
 * 负责初始化 Vigil 监控组件的自动配置，包括：
 * - 扫描并注册 Vigil 相关组件
 * - 配置 Logback Appender 以支持实时日志流
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 * @date 2025/9/17
 */
@Configuration
@ComponentScan(basePackages = "org.htx.vigil")
public class VigilConfiguration {
    
    /** 日志事件发布器，用于实时日志流传输 */
    private final LogEventPublisher publisher;

    /**
     * 构造函数，初始化 Vigil 配置
     * 
     * @param publisher 日志事件发布器，不能为 null
     */
    public VigilConfiguration(LogEventPublisher publisher) {
        this.publisher = publisher;
        configureLogbackAppender();
    }

    /**
     * 配置 Logback Appender
     * 
     * 查找已存在的 LogbackAppender 实例，并设置日志事件发布器。
     * 如果未找到现有实例，则不进行任何操作（避免重复添加 Appender）。
     */
    private void configureLogbackAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 遍历 ROOT logger 的所有 Appender，查找 LogbackAppender 实例
        for (Iterator<Appender<ILoggingEvent>> it = context.getLogger("ROOT").iteratorForAppenders(); it.hasNext(); ) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof LogbackAppender) {
                // 找到现有实例，设置发布器并退出
                ((LogbackAppender) appender).setPublisher(publisher);
                return;
            }
        }
        // 如果未找到现有 LogbackAppender，则不进行任何操作
        // 这样可以避免重复添加 Appender 到 ROOT logger
    }
}
