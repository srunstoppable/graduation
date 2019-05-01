package com.example.graduation.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Record;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {


    @Select("select * from record order by begintime desc")
    List<Record> list(Page<Record> page);

    @Select("select count(*) from record")
    int seelctTotal();
}
