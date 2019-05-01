package com.example.graduation.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Park;
import com.baomidou.mybatisplus.service.IService;
import com.example.graduation.response.Response;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sr
 * @since 2019-01-21
 */
public interface ParkService extends IService<Park> {
    /**
     * 查询所有车位信息
     *
     * @param page
     * @return
     */
    public Response parks(Page<Park> page);

    /**
     * 首页查询车位信息。
     *
     * @return
     */
    public Response parkStatus();

    /**
     * 管理员根据实际情况（车位是否有损坏）调整车位。
     *
     * @param park
     */
    public Response update(Park park);

    /**
     * 获取单个区域车位的信息
     *
     * @param id
     * @return
     */
    public Park get(String id);

    /**
     * 是否还有大车位
     *
     * @return
     */
    public boolean bigIsLeft();

    /**
     * 是否号有小车为
     *
     * @return
     */
    public boolean smallIsLeft();


    public List<Park> smallList();

    public List<Park> bigList();


    //用于招最短路径时返回路口对应的区域
    public Park findBig(int vertex);

    public Park findSmall(int vertex);


    public List<Park> findSmallList(int vertex);
    public List<Park> findBigList(int vertex);

    public void in(String id);
    public  void out(String id);
}
