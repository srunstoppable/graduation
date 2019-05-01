package com.example.graduation.service;

import com.example.graduation.entity.Record;
import com.example.graduation.entity.Ways;
import com.baomidou.mybatisplus.service.IService;
import com.example.graduation.response.Response;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sr
 * @since 2019-03-03
 */
public interface WaysService extends IService<Ways> {
    public void add(Ways ways);

    public String out(Record record);

    public String out(String id);

}
