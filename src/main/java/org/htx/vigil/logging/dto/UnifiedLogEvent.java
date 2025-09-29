package org.htx.vigil.logging.dto;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.springframework.util.StringUtils;

/**
 * 统一日志事件实体
 * 
 * 封装日志事件的所有信息，包括日志级别、消息内容、时间戳、线程信息等。
 * 支持异常信息的序列化，提供包名过滤功能。
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 * @date 2025/9/18
 */
public class UnifiedLogEvent {

    /** 日志级别（如 INFO、ERROR、DEBUG 等） */
    private String level;
    
    /** 日志记录器名称 */
    private String loggerName;
    
    /** 日志消息内容 */
    private String message;
    
    /** 日志时间戳（毫秒） */
    private Long timestamp;
    
    /** 线程名称 */
    private String threadName;

    /** 异常消息（如果有异常） */
    private String throwableMessage;
    
    /** 异常堆栈跟踪信息（如果有异常） */
    private String stackTrace;

    /**
     * 清空对象内容
     * 
     * 重置所有字段为初始值，用于对象池复用。
     */
    public void clear() {
        this.level = null;
        this.loggerName = null;
        this.message = null;
        this.timestamp = 0L;
        this.threadName = null;
        this.throwableMessage = null;
        this.stackTrace = null;
    }

    /**
     * 填充日志信息
     * 
     * 从 Logback 日志事件中提取信息并填充到当前对象中。
     * 支持异常信息的处理和包名过滤。
     * 
     * @param level 日志级别
     * @param loggerName 日志记录器名称
     * @param message 日志消息
     * @param timestamp 时间戳
     * @param threadName 线程名称
     * @param throwable 异常代理对象，可能为 null
     * @param packageName 包名过滤器，用于过滤堆栈跟踪信息
     */
    public void reset(String level, String loggerName, String message, long timestamp,
                      String threadName, IThrowableProxy throwable, String packageName) {
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.timestamp = timestamp;
        this.threadName = threadName;

        // 处理异常信息
        if (throwable != null) {
            this.throwableMessage = throwable.getClassName() + ": " + throwable.getMessage();
            this.stackTrace = getStackTraceString(throwable, packageName);
        } else {
            this.throwableMessage = null;
            this.stackTrace = null;
        }
    }

    /**
     * 获取堆栈跟踪信息字符串
     * 
     * 根据包名过滤器提取相关的堆栈跟踪信息。
     * 如果包名为空，则只返回第一行堆栈信息。
     * 
     * @param throwable 异常代理对象
     * @param packageName 包名过滤器
     * @return 过滤后的堆栈跟踪字符串
     */
    private String getStackTraceString(IThrowableProxy throwable, String packageName) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElementProxy element : throwable.getStackTraceElementProxyArray()) {

            // 如果包名为空，只返回第一行堆栈信息
            if(StringUtils.isEmpty(packageName)){
                sb.append(element.getSTEAsString()).append("\n");
                break;
            }

            // 如果堆栈元素包含指定包名，则添加到结果中
            if (element.getSTEAsString().contains(packageName)){
                sb.append(element.getSTEAsString()).append("\n");
            }else {
                // 遇到不包含包名的元素时停止
                break;
            }
        }
        return sb.toString();
    }

    // ================= Getter 方法 =================
    
    /** 获取日志级别 */
    public String getLevel() { return level; }
    
    /** 获取日志记录器名称 */
    public String getLoggerName() { return loggerName; }
    
    /** 获取日志消息内容 */
    public String getMessage() { return message; }
    
    /** 获取日志时间戳 */
    public Long getTimestamp() { return timestamp; }
    
    /** 获取线程名称 */
    public String getThreadName() { return threadName; }
    
    /** 获取异常消息 */
    public String getThrowableMessage() { return throwableMessage; }
    
    /** 获取异常堆栈跟踪信息 */
    public String getStackTrace() { return stackTrace; }
}
