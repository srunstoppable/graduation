package com.example.graduation.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author s r
 * @date 2019/1/22
 */
@Data
@Accessors(chain = true)
public class ParkResult {
    /**
     * 总车位
     */
    private int total;
    /**
     * 可用小车为位
     */
    private int usableSmall;
    /**
     * 可用大车位
     */
    private int usableBig;
    /**
     * 固定收费
     */
    private String fixedCharge;

    /**
     * 超时收费
     */
    private String changedCharge;

    private String fixedDay;
}
