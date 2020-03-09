package com.atguigu.guli.service.order.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.order.client.CourseClient;
import com.atguigu.guli.service.order.client.MemberClient;
import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.entity.PayLog;
import com.atguigu.guli.service.order.mapper.OrderMapper;
import com.atguigu.guli.service.order.mapper.PayLogMapper;
import com.atguigu.guli.service.order.service.OrderService;
import com.atguigu.guli.service.order.util.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ZYL
 * @since 2020-03-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private PayLogMapper payLogMapper;

    @Override
    public String saveOrder(String courseId, String memberId) {

        //根据课程id和用户id查询是否订单已存在
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if(orderExist != null){
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }

        //查询课程相关信息
        CourseDto courseDto = courseClient.getCourseDtoById(courseId);
        if (courseDto == null){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //查询会员相关信息
        MemberDto memberDto = memberClient.getMemberDtoByMemberId(memberId);
        if (memberDto == null){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);

        return order.getId();
    }

    @Override
    public Boolean isBuyByCourseId(String memberId, String courseId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId)
                .eq("course_id", courseId)
                .eq("status",1);//已支付

        Integer count = baseMapper.selectCount(queryWrapper);

        return count.intValue() > 0;
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId)
                .eq("member_id", memberId);
        Order order = baseMapper.selectOne(queryWrapper);

        return order;
    }

    /**
     * 根据订单号查询订单实体
     * @param orderNo
     * @return
     */
    @Override
    public Order getOrderByOrderNo(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 支付成功更改订单状态
     * @param map
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //更新本地课程订单状态
        String orderNo = map.get("out_trade_no");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1);
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee().longValue());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(map));
        payLogMapper.insert(payLog);//插入到支付日志表

        //更改课程购买数量
        //TODO
    }
}
