package org.htx.vigil.logging.controller;

import org.htx.vigil.logging.LogEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 日志流式传输控制器
 * 
 * 提供基于 Server-Sent Events (SSE) 的实时日志流接口。
 * 客户端可以通过此接口订阅实时日志事件流。
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 */
@RestController
@RequestMapping("/vigil")
@CrossOrigin
public class LogSSEController {

    /** 日志事件发布器，用于获取日志事件流 */
    private final LogEventPublisher publisher;

    /**
     * 构造函数
     * 
     * @param publisher 日志事件发布器，不能为 null
     */
    public LogSSEController(LogEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 获取实时日志流
     * 
     * 返回一个基于 SSE 的日志事件流，客户端可以实时接收日志事件。
     * 使用 TEXT_EVENT_STREAM 媒体类型支持 SSE 协议。
     * 
     * @return Flux<String> 日志事件的 JSON 字符串流
     */
    @GetMapping(value = "/logs/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLogs() {
        return publisher.getStream();
    }
}
