package org.htx.vigil.logging.appender;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.catalina.core.ApplicationContext;
import org.htx.vigil.logging.LogEventPublisher;
import org.htx.vigil.logging.dto.UnifiedLogEvent;

/**
 * Logback 自定义 Appender
 * 
 * 继承自 Logback 的 AppenderBase，用于捕获日志事件并通过 LogEventPublisher 发布。
 * 支持包名过滤，只处理指定包下的日志事件。
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 * @date 2025/9/17
 */
public class LogbackAppender extends AppenderBase<ILoggingEvent> {

    /** 包名过滤器，只处理指定包下的日志事件，null 表示处理所有日志 */
    private String packageName = null;
    
    /** 日志事件发布器，用于将日志事件发布到响应式流 */
    private LogEventPublisher publisher;

    /**
     * 设置日志事件发布器
     * 
     * @param publisher 日志事件发布器，不能为 null
     */
    public void setPublisher(LogEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 处理日志事件
     * 
     * 将 Logback 的日志事件转换为 UnifiedLogEvent 并通过发布器发布。
     * 只有在 Appender 已启动且发布器不为空时才处理事件。
     * 
     * @param eventObject Logback 日志事件对象
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        // 检查 Appender 状态和发布器是否可用
        if (!isStarted() || publisher == null) return;

        // 创建统一的日志事件对象
        UnifiedLogEvent logEvent = new UnifiedLogEvent();
        logEvent.reset(
                eventObject.getLevel().toString(),
                eventObject.getLoggerName(),
                eventObject.getFormattedMessage(),
                eventObject.getTimeStamp(),
                eventObject.getThreadName(),
                eventObject.getThrowableProxy(),
                packageName
        );

        // 发布日志事件
        publisher.publish(logEvent);
    }

    /**
     * 设置包名过滤器
     * 
     * @param packageName 要过滤的包名，null 表示不过滤
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}