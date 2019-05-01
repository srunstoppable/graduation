package com.example.graduation.service;

import com.example.graduation.entity.Vertex;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sr
 * @since 2019-02-15
 */
public interface VertexService extends IService<Vertex> {
    public Vertex findV(int i);
}
