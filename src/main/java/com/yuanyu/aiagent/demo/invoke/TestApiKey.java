package com.yuanyu.aiagent.demo.invoke;

/**
 * 仅用于测试获取 API KEY
 */
public interface TestApiKey {
    String API_KEY = System.getenv("DASHSCOPE_API_KEY");
}
