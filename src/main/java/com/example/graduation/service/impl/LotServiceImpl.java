package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Lot;
import com.example.graduation.entity.Park;
import com.example.graduation.entity.Record;
import com.example.graduation.mapper.LotMapper;
import com.example.graduation.response.Response;
import com.example.graduation.result.LotUpdate;
import com.example.graduation.service.LotService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.graduation.service.ParkService;
import com.example.graduation.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 *
 * @author sr
 * @since 2019-02-20
 */
@Service
public class LotServiceImpl extends ServiceImpl<LotMapper, Lot> implements LotService {

    @Autowired
    private ParkService parkService;

    @Autowired
    private RecordService recordService;
    @Override
    public Response lotes(Page<Lot> page) {
        Map<Integer, Object> map = new TreeMap();
        for (Lot lot : baseMapper.lots(page)) {
            map.put(lot.getId(), lot);
        }
        return Response.success(baseMapper.selectTotal()).putAllT(map);
    }

    //找出区域的精确车位
    @Override
    public Lot findEmpty(String id) {
        EntityWrapper<Lot> ew=new EntityWrapper<>();
        ew.eq("empty","yes");
        ew.eq("parkid",id);
        List<Lot> lots = selectList(ew);
        if(lots.size() == 0){
            return null;
        }
        return lots.get(0);
    }

    @Override
    public List<Lot> findEmptyList(String id) {
        List<Lot> list = new ArrayList<>();
        EntityWrapper<Lot> ew=new EntityWrapper<>();
        ew.eq("empty","yes");
        ew.eq("parkid",id);
        ew.orderBy("code");
        List<Lot> lots = selectList(ew);
        if(lots.size() == 0){
            return null;
        }
        list.add(lots.get(0));
        list.add(lots.get(lots.size()-1));
        return list;
    }

    @Override
    public void in(Lot lot) {
        updateAllColumnById(lot);
    }

    @Override
    public void out(Lot lot) {
        updateById(lot);
    }

    @Override
    public Lot find(int id, String parkid) {
        EntityWrapper<Lot> ew=new EntityWrapper<>();
        ew.eq("code",id).eq("parkid",parkid);
        return selectOne(ew);
    }

    @Override
    public Response updateLot(LotUpdate lotUpdate) {
        try {
            Lot lot = find(lotUpdate.getCode(), lotUpdate.getParkid());
            lot.setEmpty("yes").setCarid("");
            updateAllColumnById(lot);
            parkService.out(lotUpdate.getParkid());
            Lot lot2 = find(lotUpdate.getCodeUp(), lotUpdate.getParkidUp());
            lot2.setCarid(lotUpdate.getCarid()).setEmpty("no");
            updateAllColumnById(lot2);
            parkService.in(lotUpdate.getParkidUp());

            EntityWrapper<Record>ew =new EntityWrapper<>();
            ew.eq("id",lotUpdate.getCarid());
            Record record =recordService.selectOne(ew);
            record.setParkid(lotUpdate.getParkidUp()).setCode(lotUpdate.getCodeUp());
            recordService.updateAllColumnById(record);
            return Response.success();
        }
        catch (Exception e){
            return Response.fail("更新失败!");
        }

    }


}
