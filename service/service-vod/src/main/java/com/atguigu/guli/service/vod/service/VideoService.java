package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface VideoService {

    /**
     * 执行视频上传
     * @param inputStream
     * @param originalFilename
     * @return  视频id
     */
    String uploadVideo(InputStream inputStream, String originalFilename);

    /**
     * 删除上传视频
     * @param vodId
     * @throws ClientException
     */
    void removeVideo(String vodId) throws ClientException, com.aliyuncs.exceptions.ClientException;

    /**
     * 获取上传地址和凭证
     * @return
     */
    Map<String, Object> getVideoUploadAuthAndAdress(String title, String fileName) throws ClientException;

    Map<String, Object> refreshVideoUploadAuth(String videoId) throws ClientException;

    String getVideoPlayAuth(String videoId) throws ClientException;

    void removeVideoByIdList(List<String> videoSourceIdList) throws ClientException;
}
