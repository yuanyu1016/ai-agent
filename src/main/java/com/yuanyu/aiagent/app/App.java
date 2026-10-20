package com.yuanyu.aiagent.app;

import com.yuanyu.aiagent.advisor.MyLoggerAdvisor;
import com.yuanyu.aiagent.advisor.ReReadingAdvisor;
import com.yuanyu.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class App {
    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "扮演全方位健康生活平台的专家。开场向用户表明身份，告知用户可倾诉关于身体健康和心理健康的难题。"+
            "在身体健康方面，询问用户的健身目标、饮食习惯以及是否需要饮食计划、食谱或营养补充品的建议。"+
            "围绕健身活动，分类询问有氧运动、力量训练和柔韧性训练的需求，标明难度等级和目标。" +
            "在心理健康方面，询问用户的情绪状态，以及是否需要在线咨询、自助工具或冥想与放松练习的支持。"+
            "根据用户的反馈，提供个性化的建议与解决方案，确保用户在身体和心理健康方面得到全面的指导与支持。";

    /**
     * 初始化 ChatClient
     * @param dashscopeChatModel
     */
    public App(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        // ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志拦截器，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强拦截器，可按需开启
                        // new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 基础对话（支持多轮对话记忆，SSE流式传输）
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }

    record Report(String title, List<String> suggestions) {

    }

    /**
     * AI 对话（格式化输出，生成报告）
     * @param message
     * @param chatId
     * @return
     */
    public Report doChatWithReport(String message, String chatId) {
        Report report = chatClient
                .prompt()
                .system(SYSTEM_PROMPT+"每次对话后都要生成结果报告，标题为{用户名}的健康生活报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(Report.class);
        log.info("report: {}", report);
        return report;
    }

//    // AI知识库问答功能
//    @Resource
//    private VectorStore appVectorStore;
//    // 阿里云知识库
//    @Resource
//    private Advisor appRagCloudAdvisor;

    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用 RAG 知识库问答
//                 .advisors(new QuestionAnswerAdvisor(appVectorStore))
                // 应用增强检索服务（云知识库服务）
//                .advisors(appRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI 调用工具能力
    @Resource
    private ToolCallback[] alltools;

    /**
     * AI 报告功能（支持调用工具）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // 开启工具调用
                .tools(alltools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

//    // AI 调用 MCP 服务
//    @Resource
//    private ToolCallbackProvider toolCallbackProvider;
//
//    /**
//     * AI 报告功能（调用 MCP 服务）
//     * @param message
//     * @param chatId
//     * @return
//     */
//    public String doChatWithMcp(String message, String chatId) {
//        ChatResponse chatResponse = chatClient
//                .prompt()
//                .user(message)
//                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
//                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                // 开启日志
//                .advisors(new MyLoggerAdvisor())
//                // 开启工具调用
//                .tools(toolCallbackProvider)
//                .call()
//                .chatResponse();
//        String content = chatResponse.getResult().getOutput().getText();
//        log.info("content: {}", content);
//        return content;
//    }

    // AI 调用 MCP 服务
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

//    @Resource
//    private SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;
    // 和 Spring AI 的工具进行整合
//    @Autowired
//    private SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;
//    ToolCallback[] toolCallbacks = syncMcpToolCallbackProvider.getToolCallbacks();

    /**
     * AI 报告功能（调用 MCP 服务）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // 开启工具调用
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = null;
        if (chatResponse != null) {
            content = chatResponse.getResult().getOutput().getText();
        }
        log.info("content: {}", content);
        return content;
    }
}
