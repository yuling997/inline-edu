package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.from.VideoInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author ZYL
 * @since 2020-02-12
 */
public interface VideoService extends IService<Video> {

    void saveVideoInfo(VideoInfoForm videoInfoForm);

    VideoInfoForm getVideoInfoFormById(String id);

    void updateCourseInfoById(VideoInfoForm videoInfoForm);

    void removeVideoById(String id);
}
