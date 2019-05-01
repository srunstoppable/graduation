package com.example.graduation.service;

import com.example.graduation.entity.Park;
import com.example.graduation.entity.Path;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sr
 * @since 2019-01-29
 */
public interface PathService extends IService<Path> {
    public List<Path> paths();

    public List shortPath();


    public List shortPath(int i);

    public Path find(int i, int j);
}
