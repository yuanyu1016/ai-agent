package com.yuanyu.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppTest {

    @Resource
    private  App app;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是何元玉";
        String answer = app.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我是中科大的研究生，最近因为科研和毕业压力感到焦虑";
        answer = app.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我是什么大学的学生？帮我回忆一下";
        answer = app.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是何元玉，我正在读研，并且有焦虑的问题，但我不知道该怎么做";
        App.Report report = app.doChatWithReport(message, chatId);
        Assertions.assertNotNull(report);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我该如何定制饮食计划，三餐应该如何摄入？";
        String answer = app.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末我想和朋友去杭州旅游放松心情，推荐几个适合的景点或餐厅？");
//
//        // 测试网页抓取
//        testMessage("最近在健身，但是背部肌肉训练效果不明显，看看musclewiki网站（https://musclewiki.cn/）有什么推荐的动作？");
//
//        // 测试资源下载：图片下载
//        testMessage("直接下载一张风景图片为文件");
//
//        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");
//
//        // 测试文件操作：保存用户档案
//        testMessage("保存我的健康建议档案为文件");

        // 测试 PDF 生成
//        testMessage("生成一份‘周末健康生活计划{今天日期}’PDF，包含自助食谱、健身计划、去杭州旅游小憩地点和门票信息、餐厅预订、活动流程和交通信息");

        // 测试发送邮件
//        testMessage("直接发送邮件给我的学校邮箱（heyuanyu@mail.ustc.edu.cn），内容是我的一周健康饮食和运动建议。");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = app.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // stdio模式测试地图和图片搜索都有bug，暂时没解决
//         测试地图 MCP
//        String message = "我居住在合肥中科大高新区，周末想要区杭州旅游放松心情，请帮我找到西湖附近的旅游地点，并规划出行路线。";
        String message = "我居住在合肥蜀山区，帮我推荐五公里内的餐厅";
        String answer =  app.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
        // 测试图片搜索 MCP
        message = "帮我搜索一些杭州的风景图片";
        answer =  app.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}


