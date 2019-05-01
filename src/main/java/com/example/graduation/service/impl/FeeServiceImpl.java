package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.graduation.entity.Fee;
import com.example.graduation.mapper.FeeMapper;
import com.example.graduation.service.FeeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {

    @Override
    public List<Fee> fees() {
        EntityWrapper<Fee> ew = new EntityWrapper<>();
        return selectList(ew);
    }

    @Override
    public void update(Fee fee) {
        updateById(fee);
    }
}
