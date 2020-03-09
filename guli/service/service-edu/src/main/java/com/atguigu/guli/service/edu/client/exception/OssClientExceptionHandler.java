package com.atguigu.guli.service.edu.client.exception;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.client.OssClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OssClientExceptionHandler implements OssClient {
    @Override
    public R removeFile(String url) {

        //错误日志
        log.error("熔断器被执行");
        return R.ok();
    }
}
