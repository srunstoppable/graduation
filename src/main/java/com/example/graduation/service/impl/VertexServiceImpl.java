package com.example.graduation.service.impl;

import com.example.graduation.entity.Vertex;
import com.example.graduation.mapper.VertexMapper;
import com.example.graduation.service.VertexService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-02-15
 */
@Service
public class VertexServiceImpl extends ServiceImpl<VertexMapper, Vertex> implements VertexService {

    @Override
    public Vertex findV(int i) {
        return selectById(i);
    }
}
