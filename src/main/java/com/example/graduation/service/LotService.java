package com.example.graduation.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Lot;
import com.baomidou.mybatisplus.service.IService;
import com.example.graduation.response.Response;
import com.example.graduation.result.LotUpdate;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sr
 * @since 2019-02-20
 */
public interface LotService extends IService<Lot> {
    public Response lotes(Page<Lot>page);
    public Lot findEmpty(String id);
    public List<Lot>findEmptyList(String id);
    public void in(Lot lot);
    public void out(Lot lot);
    public Lot find(int id,String parkid);
    public Response updateLot(LotUpdate lotUpdate);


}
