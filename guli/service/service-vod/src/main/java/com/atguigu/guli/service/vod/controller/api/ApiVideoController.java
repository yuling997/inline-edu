package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@Api(description = "阿里云视频播放")
//@CrossOrigin
@RestController
@RequestMapping("/web/vod/video")
@Slf4j
public class ApiVideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "获取视频播放凭证")
    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuthByVideoId(
            @ApiParam(name = "videoSourceId", value = "阿里云视频文件的ID", required = true)
            @PathVariable String videoSourceId
    ) {

        try {
            String playAuth = videoService.getVideoPlayAuth(videoSourceId);
            return R.ok().data("playAuth", playAuth).message("获取播放凭证成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }
}
