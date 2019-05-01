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
public class Park implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("usable")
    private Integer usable;
    @TableField("totalcount")
    private Integer totalcount;
    @TableField("type")
    private String type;
    @TableField("leftup")
    private Integer leftup;
    @TableField("leftdown")
    private Integer leftdown;
    @TableField("rightup")
    private Integer rightup;
    @TableField("rightdown")
    private Integer rightdown;
    @TableField("floor")
    private String floor;


}
