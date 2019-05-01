package com.example.graduation.util;

import java.util.List;

/**
 * @author s r
 * @date 2019/1/30
 */
public interface ListRe {
    Object value(int pos);
    boolean add(Object obj,int pos);
    Object remove(int pos);
    int find(Object obj);
    boolean modify(Object obj,int pos);
    boolean isEmpty();
    int size();
    List forward();
    void backward();
    void clear();
    ListRe sort();
}
