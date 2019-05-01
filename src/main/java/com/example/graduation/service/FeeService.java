package com.example.graduation.service;

import com.example.graduation.entity.Fee;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
public interface FeeService extends IService<Fee> {
    /**
     * 获取费用信息
     *
     * @return
     */
    public List<Fee> fees();

    /**
     * 更新费用信息
     *
     * @param fee
     */
    public void update(Fee fee);

}
