package com.yuanyu.aiagent.tools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
public class EmailSenderToolTest {
    // 我的邮箱
    @Value("${email.from-email}")
    private String fromEmail;

    // 我的的SMTP授权码（16位字符）
    @Value("${email.auth-code}")
    private String authCode;

    @Test
    void sendTextEmail() {
        EmailSenderTool emailSenderTool = new EmailSenderTool(fromEmail,authCode);
        try {
            emailSenderTool.sendTextEmail(
                    "heyuanyu@mail.ustc.edu.cn",  // 替换为目标邮箱
                    "测试邮件",
                    "这是一封通过AI发送的测试邮件。"
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
