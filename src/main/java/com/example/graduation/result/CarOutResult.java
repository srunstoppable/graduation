package com.example.graduation.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author s r
 * @date 2019/1/22
 */
@Data
@Accessors(chain = true)
public class CarOutResult {
    /**
     * 入场时间
     */
    @ApiModelProperty("入场时间")
    private String beginTime;
    /**
     * 离场时间
     */
    @ApiModelProperty("离场时间")
    private String outTime;
    /**
     * 停车费用
     */
    @ApiModelProperty("停车费用")
    private double fee;

    /**
     * 离场路线
     */
    @ApiModelProperty("离场路线")
    private String way;
}
