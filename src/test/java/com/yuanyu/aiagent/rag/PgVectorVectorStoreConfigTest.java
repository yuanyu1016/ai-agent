package com.yuanyu.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PgVectorVectorStoreConfigTest {

    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("中国科大是一座位于合肥的985大学，以强劲的理工科吸引全国学子。红专并进，科教报国，是其理念。", Map.of("meta1", "meta1")),
                new Document("科研的真谛在于理论和实践。发现问题，然后解决问题"),
                new Document("深蹲和硬拉是非常有效的下肢力量训练动作。", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("研究生").topK(5).build());
        Assertions.assertNotNull(results);
    }
}
