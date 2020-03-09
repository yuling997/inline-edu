package com.atguigu.guli.service.edu.client.exception;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.client.VodClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class VodClientExceptionHandler implements VodClient {


    @Override
    public R removeVideoByIdList(List<String> videoSourceIdList) {

        //错误日志
        log.error("熔断器被执行");
        return R.ok();
    }
}
