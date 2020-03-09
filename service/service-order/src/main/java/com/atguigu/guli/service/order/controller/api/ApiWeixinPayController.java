package com.atguigu.guli.service.order.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.order.service.OrderService;
import com.atguigu.guli.service.order.service.WeixinPayService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(description = "网站微信支付")
@RestController
@CrossOrigin
@RequestMapping("/web/weixin-pay")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/create-native/{noderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){

        String remoteAddr = request.getRemoteAddr();
        Map map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
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

    /**
     * 查询支付状态：此方法会被轮询调用（被定时器调用）
     * @param orderNo
     * @return
     */
    @GetMapping("/query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        Map<String, String> map = weixinPayService.queryPayStatus(orderNo);
        if("SUCCESS".equals(map.get("trade_state"))){//支付
            //如果支付成功则修改订单状态
            orderService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN); //支付中.....
    }
}
