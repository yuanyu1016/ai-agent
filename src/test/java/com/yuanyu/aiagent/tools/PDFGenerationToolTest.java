package com.yuanyu.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "ai健康智能体项目.pdf";
        String content = "何元玉 ai健康智能体项目";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
