package com.example.graduation.util;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author s r
 * @date 2019/1/29
 */

@Data
@Accessors(chain = true)
public class EdgeNode {  //定义邻接表中的边节点类型
    int adjvex;  //邻节点域
    int weight;  //边的权值
    EdgeNode next;  //指向下一个边界点的链接域
}
