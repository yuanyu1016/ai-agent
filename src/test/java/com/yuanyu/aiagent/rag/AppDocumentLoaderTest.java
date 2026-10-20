package com.yuanyu.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppDocumentLoaderTest {

    @Resource
    private AppDocumentLoader appDocumentLoader;

    @Test
    void loadMarkdowns() {
        appDocumentLoader.loadMarkdowns();
    }

}