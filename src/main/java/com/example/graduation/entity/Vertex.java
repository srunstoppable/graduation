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
 * @since 2019-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Vertex implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;
    @TableField("upver")
    private Integer upver;
    @TableField("downver")
    private Integer downver;
    @TableField("leftver")
    private Integer leftver;
    @TableField("rightver")
    private Integer rightver;
    @TableField("abover")
    private Integer abover;
    @TableField("blowver")
    private Integer blowver;


}
