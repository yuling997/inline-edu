package com.atguigu.guli.service.order.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.order.service.AliPayService;
import com.atguigu.guli.service.order.service.OrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(description = "网站支付宝支付")
@RestController
@CrossOrigin
@RequestMapping("/web/aliyun-pay")
@Slf4j
public class ApiAliPayController {


    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private OrderService orderService;

    /**
     * 生成二维码
     *
     * @return
     */
    @GetMapping("/create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {

        String map = aliPayService.createNative(orderNo, request);
        return R.ok().data(map,"map");
    }

    /**
     * 支付回调
     * @return
     */
    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response){

        System.out.println("callback/notify 被调用");
        //1、获取支付结果：从request中获取
        //2、验签：商户系统对于支付结果通知的内容一定要做签名验证,
        //3、判断订单状态
        //4、如果支付成功，则更新支付状态
        //5、准备返回值 xml SUCCESS 结果：通过response返回
        return null;
    }



}
