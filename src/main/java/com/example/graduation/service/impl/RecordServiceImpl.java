package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Fee;
import com.example.graduation.entity.Lot;
import com.example.graduation.entity.Park;
import com.example.graduation.entity.Record;
import com.example.graduation.mapper.RecordMapper;
import com.example.graduation.response.Response;
import com.example.graduation.result.CarOutResult;
import com.example.graduation.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private FeeService feeService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private LotService lotService;

    @Autowired
    private WaysService waysService;

    @Override
    public boolean in(Record record) {

        if (record.getId().length() != 7) {
            return false;
        }
        /**
         * 车辆进场 寻找最近空位并提供路线
         * TODO
         */
        try {


            if (insert(record.setBegintime(new Date()))) {
                parkService.in(record.getParkid());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public CarOutResult out(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Record record = selectById(id);
        parkService.out(record.getParkid());
        CarOutResult carOutResult = new CarOutResult();
        //设置 入场和出场时间
        carOutResult.setBeginTime(sdf.format(record.getBegintime()))
                .setOutTime(sdf.format(date))
                .setWay(waysService.out(record));
        //计算规则  先看是否满一天，如果满，则结算一天的费用，如果未满，则看是否满固定收费时长。
        double hour = (double) (date.getTime() - record.getBegintime().getTime()) / (1000 * 60 * 60); //小时
        int day = (int) Math.floor((double) hour / 24);   //天,向下取整
        List<Fee> feeList = feeService.fees();
        deleteById(record.getId());
        Lot lot = lotService.find(record.getCode(), record.getParkid());
        lot.setEmpty("yes").setCarid("");
        lotService.out(lot);
        if (hour > 24) {
            int time = (int) Math.ceil(hour - (24 * day));
            return carOutResult.setFee(feeList.get(2).getPrice() * day + time * feeList.get(1).getPrice());
        } else if (hour <= feeList.get(0).getTime()) {
            return carOutResult.setFee(feeList.get(0).getPrice());
        } else {
            int i = (int) Math.ceil(hour - feeList.get(0).getTime());
            return carOutResult.setFee(feeList.get(0).getPrice() + feeList.get(1).getPrice() * (i / feeList.get(0).getTime()));
        }

    }

    @Override
    public Response list(Page<Record> page) {
        Map<String, Object> map = new TreeMap<>();
        List<Record> list = baseMapper.list(page);
        if (list == null) {
            return Response.success(Collections.EMPTY_LIST);
        }
        for (Record record : list) {
            map.put(record.getId(), record);
        }
        return Response.success(baseMapper.seelctTotal()).putAll(map);

    }

}
