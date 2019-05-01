package com.example.graduation.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Park;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sr
 * @since 2019-01-21
 */
@Mapper
public interface ParkMapper extends BaseMapper<Park> {


    @Select("select * from park")
    List<Park> list(Page<Park> page);

    @Select("select count(*) from park")
    int selectTotal();

    @Select("select * from park where (leftup = #{vertex} or leftdown = #{vertex} or rightup = #{vertex} or rightdown = #{vertex}) and type ='big' and usable>0 ")
    public List<Park> findBig(@Param("vertex") int vertex);

    @Select("select * from park where (leftup = #{vertex} or leftdown = #{vertex} or rightup = #{vertex} or rightdown = #{vertex}) and type ='small' and usable>0 ")
    public  List<Park> findSmall(@Param("vertex") int vertex);
}
