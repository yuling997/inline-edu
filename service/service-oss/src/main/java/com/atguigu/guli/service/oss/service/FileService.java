package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {

    /**
     * 文件上传至阿里云
     */
    String upload(InputStream inputStream, String module, String filename);

    /**
     * 删除文件
     */
    void removeFile(String url);
}
