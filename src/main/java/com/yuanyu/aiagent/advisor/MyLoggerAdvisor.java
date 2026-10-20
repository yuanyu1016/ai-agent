package com.yuanyu.aiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志 Advisor
 * 打印 info 级别日志、只输出单次用户提示词和 AI 回复的文本
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("AI Request: {}", request.userText());
        return request;
    }

//    private AdvisedRequest before(AdvisedRequest request) {
//        String userText = request.userText();
//        try {
//            // 检查请求长度必须在 1 到 129024 之间
//            if (userText.length() < 1 || userText.length() > 129024) {
//                throw new IllegalArgumentException("The request length must be between 1 and 129024.");
//            }
//        } catch (IllegalArgumentException e) {
//            // 记录异常信息，并在这里处理，比如设置默认值或返回一个特定的响应
//            log.error("Request Error: {}", e.getMessage());
//            // 可以选择返回一个默认的请求或做其他处理,这里选择截断
//            userText = userText.substring(0, 129024);
//        }
//        log.info("AI Request: {}", userText);
//        return request;
//    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
