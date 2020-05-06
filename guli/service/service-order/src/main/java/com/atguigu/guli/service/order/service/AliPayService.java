package com.atguigu.guli.service.order.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AliPayService {

    /**
     *
     * @param orderNo
     * @param request
     * @return
     * @throws Exception
     */
    String createNative(String orderNo, HttpServletRequest request) ;

    /**
     * 根据订单号去微信服务器查询支付状态
     * @param orderNo
     * @return
     */
    Map<String, String> queryPayStatus(String orderNo);
}
