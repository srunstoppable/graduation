package com.example.graduation.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Lot;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sr
 * @since 2019-02-20
 */
@Mapper
public interface LotMapper extends BaseMapper<Lot> {
    @Select("select * from lot")
    public List<Lot> lots(Page<Lot>page);

    @Select("select count(*) from lot")
    public int selectTotal();

}
