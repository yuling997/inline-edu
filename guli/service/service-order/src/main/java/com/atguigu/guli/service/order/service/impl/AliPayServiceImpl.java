package com.atguigu.guli.service.order.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayUtils;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.service.AliPayService;
import com.atguigu.guli.service.order.service.OrderService;
import com.atguigu.guli.service.order.util.AliPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AliPayProperties aliPayProperties;

    /**
     * 根据id订单号下单，生成支付连接
     * @param orderNo
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public String createNative(String orderNo, HttpServletRequest request){

        try {
           /* //根据课程订单号获取订单
            Order order = orderService.getOrderByOrderNo(orderNo);

//            Product product = productService.getProductById(order.getProductId());

            //获得初始化的AlipayClient
            Map<String, String> params = new HashMap<>();
            params.put("appid", aliPayProperties.getApp_id());
//            params.put("nonce_str", AlipayUtils.generateNonceStr());//生成随机字符串
            params.put("body", order.getCourseTitle());
            params.put("out_trade_no", orderNo);

            //注意，这里必须使用字符串类型的参数（总金额：分）
            String totalFee = order.getTotalFee().multiply(new BigDecimal("100")).intValue() + "";
            params.put("total_fee", totalFee);

            params.put("notify_url", aliPayProperties.getNotify_url());*/

            //1、生成订单：根据订单流水号将订单取出
            Order order = orderService.getOrderByOrderNo(orderNo);

            //1、根据支付宝的配置生成一个支付客户端
            AlipayClient alipayClient = new DefaultAlipayClient(
                    aliPayProperties.getGatewayUrl(),
                    aliPayProperties.getApp_id(),
                    aliPayProperties.getMerchant_private_key(),
                    "json",
                    aliPayProperties.getCharset(),
                    aliPayProperties.getAlipay_public_key(),
                    aliPayProperties.getSign_type());

            //2、创建一个支付请求 //设置请求参数
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(aliPayProperties.getReturn_url());
            alipayRequest.setNotifyUrl(aliPayProperties.getNotify_url());


            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = orderNo;
            //付款金额，必填
            String total_amount = order.getTotalFee().multiply(new BigDecimal("100")).intValue() + "";
            //订单名称，必填
            String subject = order.getCourseTitle();
            //商品描述，可空
            String body = order.getCourseId();

            // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
            String timeout_express = "10m";

            //例子去官方api找
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                    + "\"total_amount\":\"" + total_amount + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"body\":\"" + body + "\","
                    + "\"timeout_express\":\"" + timeout_express + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");


            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();

//        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
            System.out.println("支付宝的响应："+result);
            return result;

/*            HashMap<String, Object> map = new HashMap<>();
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("out_trade_no", orderNo);

            return map;*/
        }catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        return null;
    }
}
