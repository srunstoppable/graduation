package com.example.graduation.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s r
 * @date 2019/1/30
 */
public class SequenceList implements ListRe {
    final int minSize = 10;
    private Object[] listArray;
    private int len;


    public SequenceList() {
        len = 0;
        listArray = new Object[minSize];
    }

    public SequenceList(int n) {
        if (n < minSize) n = minSize;
        len = 0;
        listArray = new Object[n];
    }


    @Override
    public Object value(int pos) {
        if (pos < 1 || pos > len) {
            return null;
        }
        return listArray[pos - 1];
    }

    @Override
    public boolean add(Object obj, int pos) {
        if (pos < 1 || pos > len + 1) {
            return false;
        }
        if (len == listArray.length) {
            Object[] p = new Object[len * 2];
            for (int i = 0; i < len; i++)
                p[i] = listArray[i];
            listArray = p;
        }
        for (int i = len - 1; i >= pos - 1; i--)
            listArray[i + 1] = listArray[i];
        listArray[pos - 1] = obj;
        len++;
        return true;

    }

    @Override
    public Object remove(int pos) {
        return null;
    }

    @Override
    public int find(Object obj) {
        return 0;
    }

    @Override
    public boolean modify(Object obj, int pos) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return len;
    }

    /**
     * 修改返回值，适应需求
     * @return
     */
    @Override
    public List forward() {
        List list = new ArrayList();
        for (int i = 0; i < len; i++) {
            list.add(listArray[i]);
        }
        return list;
    }

    @Override
    public void backward() {

    }

    @Override
    public void clear() {
        len = 0;
    }

    @Override
    public ListRe sort() {
        return null;
    }
}
