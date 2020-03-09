package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author helen
 * @since 2020/3/4
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String phone, Map<String, Object> param) {

        //校验手机号码
        if(StringUtils.isEmpty(phone) || !FormUtils.isMobile(phone)){
            log.error("请输入正确的手机号");
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        //调用短信发送SDK，创建client对象
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionid(),
                smsProperties.getKeyid(),
                smsProperties.getKeysecret());
        IAcsClient client = new DefaultAcsClient(profile);

        //组装请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionid());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", smsProperties.getSignname());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplatecode());

        //将包含验证码的mao集合转换为json字符串
        Gson gson = new Gson();
        request.putQueryParameter("TemplateParam", gson.toJson(param));
        try {

            //发送短信
            CommonResponse response = client.getCommonResponse(request);

            //得到json字符串格式的响应结果
            String data = response.getData();

            //解析json字符串格式的响应结果
            HashMap<String, String> map = gson.fromJson(data, HashMap.class);
            String code = map.get("Code");
            String message = map.get("Message");

            //控制所有短信流向限制（一分钟一条、一个小时五条、一天十条）
            if("isv.BUSINESS_LIMIT_CONTROL".equals(code)){
                log.error("短信发送过于频繁 " + "【code】" + code + ", 【message】" + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
            }

            //平台短信发送总条数限制
            if("isv.DAY_LIMIT_CONTROL".equals(code)){
                log.error("短信发送条数超出限制 " + "【code】" + code + ", 【message】" + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
            }


            //判断响应是否成功
            if(!"OK".equals(code)){
                log.error("短信发送失败 " + "【code】" + code + ", 【message】" + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
            }

        } catch (ServerException e) {

            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);

        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}
