package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.client.UcenterClient;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author ZYL
 * @since 2020-03-02
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private UcenterClient ucenterClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {

        //1.查询当日记录是否存在，存在则重新统计
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        //2.生成统计记录

        //2.1 生成注册记录
        //使用Ribbon 远程调用
//        R r = restTemplate.getForObject("http://guli-ucenter/admin/ucenter/member/count-register/{day}",
//                R.class,
//                day);

        //使用open Feign 远程调用
        R r = ucenterClient.countRegisterByDay(day);

        Integer registerCount = (Integer)r.getData().get("registerCount");

        //2.2 生成登录记录
        Integer loginCount = RandomUtils.nextInt(100, 200);
        //2.3 生成每日播放视频数记录
        Integer videoViewCount = RandomUtils.nextInt(100, 200);
        //2.4 生成每日新增课程数记录
        Integer courseCount = RandomUtils.nextInt(100, 200);

        //3、生成daily记录，插入到数据库中
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerCount);
        daily.setLoginNum(loginCount);
        daily.setVideoViewNum(videoViewCount);
        daily.setCourseNum(courseCount);

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        HashMap<String, Object> map = new HashMap<>();

        ArrayList<String> xList = new ArrayList<>(); //日期列表
        ArrayList<Integer> yList = new ArrayList<>(); //数据列表

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type, "date_calculated");
        queryWrapper.between("date_calculated", begin, end);

        List<Map<String, Object>> mapsData = baseMapper.selectMaps(queryWrapper);
        for (Map<String, Object> data : mapsData) {

            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);

            Integer count = (Integer) data.get(type);
            yList.add(count);
        }

        map.put("xData", xList);
        map.put("yData", yList);
        return map;
    }
}
