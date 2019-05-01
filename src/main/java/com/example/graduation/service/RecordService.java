package com.example.graduation.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Record;
import com.baomidou.mybatisplus.service.IService;
import com.example.graduation.response.Response;
import com.example.graduation.result.CarOutResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
public interface RecordService extends IService<Record> {
    /**
     *车辆入场
     */
    public boolean in(Record record);
    /**
     * 车辆出场
     */
    public CarOutResult out(String id);

    /**
     * 车辆在场情况
     */
    public Response list(Page<Record>page);

}
