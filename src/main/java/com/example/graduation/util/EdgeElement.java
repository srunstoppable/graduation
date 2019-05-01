package com.example.graduation.util;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author s r
 * @date 2019/1/29
 */
@Data
@Accessors(chain = true)
public class EdgeElement {  //定义边集数组中的元素类型
    int fromvex;   //边的起点域
    int endvex;   //边的中点域
    int weight;    //边的权值域

    public EdgeElement(int v1, int v2) {
        fromvex = v1;
        endvex = v2;
        weight = 1;
    }

    public EdgeElement(int v1, int v2, int wgt) {
        fromvex = v1;
        endvex = v2;
        weight = wgt;
    }
}
