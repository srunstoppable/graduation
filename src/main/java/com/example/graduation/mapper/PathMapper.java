package com.example.graduation.mapper;

import com.example.graduation.entity.Path;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sr
 * @since 2019-01-29
 */
@Mapper
public interface PathMapper extends BaseMapper<Path> {

    @Select("select * from path where (begin =#{i}  and end =#{j} ) or (begin =#{j} and end =#{i} )")
    public Path findPath(@Param("i") int i, @Param("j") int j);
}
