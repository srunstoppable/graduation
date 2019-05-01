package com.example.graduation.util;

/**
 * @author s r
 * @date 2019/1/29
 */
public interface Queue {
    void enter(Object object);

    Object leave();

    Object peek();  //返回队首元素的值;

    void clear();

    boolean isEmpty();

}
