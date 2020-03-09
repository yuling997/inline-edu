package com.atguigu.guli.service.statistics.client;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.client.exception.UcenterClientExceptionHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "guli-ucenter", fallback = UcenterClientExceptionHandler.class)
public interface UcenterClient {

    /**
     * 注意：一定要写成 @PathVariable("day") ， 圆括号中的”day" 不能拿=少
     * @param day
     * @return
     */
    @GetMapping("/admin/ucenter/member/count-register/{day}")
    R countRegisterByDay(@PathVariable("day") String day);
}
