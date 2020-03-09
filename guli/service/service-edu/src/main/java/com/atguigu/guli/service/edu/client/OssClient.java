package com.atguigu.guli.service.edu.client;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.client.exception.OssClientExceptionHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "guli-oss", fallback = OssClientExceptionHandler.class)
public interface OssClient {

    @DeleteMapping("/admin/oss/file/remove")
    public R removeFile(@RequestBody String url);
}
