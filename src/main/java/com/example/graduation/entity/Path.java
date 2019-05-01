package com.example.graduation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sr
 * @since 2019-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Path implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("begin")
    private Integer begin;
    @TableField("end")
    private Integer end;
    @TableField("lengt")
    private Integer lengt;


}
