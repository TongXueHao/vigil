package org.htx.vigil.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.htx.vigil.logging.dto.UnifiedLogEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 日志事件发布器
 * 
 * 负责将日志事件转换为 JSON 格式并通过响应式流发布给订阅者。
 * 使用 Reactor 的 Sinks 实现非阻塞的日志事件流传输。
 * 
 * @author Hao Tong Xue
 * @since 1.0.0
 */
@Component
public class LogEventPublisher {

    /** 响应式流接收器，支持重放最近的一条消息 */
    private final Sinks.Many<String> sink = Sinks.many().replay().limit(1);
    
    /** JSON 序列化器，用于将日志事件转换为 JSON 字符串 */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发布日志事件
     * 
     * 将日志事件序列化为 JSON 格式并发布到响应式流中。
     * 只有在有订阅者时才进行发布，避免不必要的序列化开销。
     * 
     * @param logEvent 要发布的日志事件，不能为 null
     */
    public void publish(UnifiedLogEvent logEvent) {
        // 只有在有订阅者时才发布事件，提高性能
        if (sink.currentSubscriberCount() > 0) {
            try {
                sink.tryEmitNext(objectMapper.writeValueAsString(logEvent));
            } catch (JsonProcessingException ignored) {
                // 忽略序列化异常，避免影响主业务流程
            }
        }
    }

    /**
     * 获取日志事件流
     * 
     * 返回一个响应式流，订阅者可以通过此流接收实时日志事件。
     * 
     * @return Flux<String> 日志事件的 JSON 字符串流
     */
    public Flux<String> getStream() {
        return sink.asFlux();
    }
}
