package com.yuanyu.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://www.ustc.edu.cn/images/19/08/05/1bv2xhbf6r/img05.jpg";
        String fileName = "ustc_image.jpg";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
