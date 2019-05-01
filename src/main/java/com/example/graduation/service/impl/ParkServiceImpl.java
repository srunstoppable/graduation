package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Fee;
import com.example.graduation.entity.Park;
import com.example.graduation.mapper.ParkMapper;
import com.example.graduation.response.Response;
import com.example.graduation.result.ParkResult;
import com.example.graduation.service.FeeService;
import com.example.graduation.service.ParkService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-01-21
 */
@Service
public class ParkServiceImpl extends ServiceImpl<ParkMapper, Park> implements ParkService {
    @Autowired
    private FeeService feeService;

    @Override
    public Response parks(Page<Park> page) {
        Map<String, Object> map = new TreeMap<>();
        for (Park park : baseMapper.list(page)) {
            map.put(park.getId(), park);
        }
        return Response.success(baseMapper.selectTotal()).putAll(map);
    }

    @Override
    public Response parkStatus() {
        ParkResult parkResult = new ParkResult();
        int count = 0;
        int small = 0;
        int big = 0;
        EntityWrapper<Park> ew = new EntityWrapper();
        List<Park> parkList = selectList(ew);
        for (Park park : parkList) {
            count += park.getTotalcount();
            if (park.getType().equals("small")) {
                small += park.getUsable();
            } else {
                big += park.getUsable();
            }
        }
        List<Fee> feeList = feeService.fees();
        parkResult.setTotal(count).setUsableBig(big).setUsableSmall(small)
                .setFixedCharge(feeList.get(0).getPrice() + "￥/" + feeList.get(0).getTime() + "小时以内")
                .setChangedCharge(feeList.get(1).getPrice() + "￥/小时")
                .setFixedDay(feeList.get(2).getPrice() + "￥/天");
        return Response.success(parkResult);
    }


    @Override
    public Response update(Park park) {
        Park p = selectById(park.getId());
        if((park.getUsable() + park.getTotalcount() - p.getTotalcount())<0){
            return Response.fail("更新失败，请查看车位使用情况!");
        }
        updateById(park.setUsable(park.getUsable() + park.getTotalcount() - p.getTotalcount()));
        return Response.success();
    }


    @Override
    public Park get(String id) {
        return selectById(id);
    }

    @Override
    public boolean bigIsLeft() {
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.eq("type", "big");
        int count = 0;
        for (Park park : selectList(ew)) {
            count += park.getUsable();
        }
        return count != 0;

    }

    @Override
    public boolean smallIsLeft() {
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.eq("type", "small");
        int count = 0;
        for (Park park : selectList(ew)) {
            count += park.getUsable();
        }
        return count != 0;
    }

    @Override
    public List<Park> smallList() {
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.gt("usable", 0);
        ew.eq("type", "small");
        return selectList(ew);
    }

    @Override
    public List<Park> bigList() {
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.gt("usable", 0);
        ew.eq("type", "big");
        return selectList(ew);
    }

    @Override
    public Park findBig(int vertex) {
       return baseMapper.findBig(vertex).get(0);
    }

    @Override
    public Park findSmall(int vertex) {
        return baseMapper.findSmall(vertex).get(0);
    }

    @Override
    public List<Park> findSmallList(int vertex) {
       return baseMapper.findSmall(vertex);
    }

    @Override
    public List<Park> findBigList(int vertex) {
        return baseMapper.findBig(vertex);
    }

    @Override
    public void in(String id) {
        Park park = get(id);
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.eq("id", park.getId());
        update(park.setUsable(park.getUsable() - 1), ew);
    }

    @Override
    public void out(String id) {
        Park park = get(id);
        EntityWrapper<Park> ew = new EntityWrapper<>();
        ew.eq("id", park.getId());
        update(park.setUsable(park.getUsable() + 1), ew);

    }


}
