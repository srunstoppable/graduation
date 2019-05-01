package com.example.graduation.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author s r
 * @date 2019/3/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LotUpdate {
    private int code;
    private String parkid;
    private String carid;
    private int codeUp;
    private String parkidUp;
}
