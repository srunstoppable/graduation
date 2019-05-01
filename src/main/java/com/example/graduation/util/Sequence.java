package com.example.graduation.util;


/**
 * @author s r
 * @date 2019/1/29
 */
public class Sequence implements Queue {
    final int minSize = 10;
    private Object queueArray[];
    private int front, rear;

    public Sequence() {
        front = rear = 0;
        queueArray = new Object[minSize];

    }

    public Sequence(int n) {
        front = rear = 0;
        if (n < minSize) n = minSize;
        queueArray = new Object[n];

    }


    @Override
    public void enter(Object object) {
        if ((rear + 1) % queueArray.length == front) {
            Object[] p = new Object[queueArray.length * 2];
            if (rear == queueArray.length - 1) {
                for (int i = 1; i < rear; i++)
                    p[i] = queueArray[i];
            } else {
                int i, j = 1;
                for (i = front + 1; i < queueArray.length; i++, j++)
                    p[j] = queueArray[i];
            }
            queueArray = p;
        }
        rear = (rear + 1) % queueArray.length;
        queueArray[rear] = object;
    }

    @Override
    public Object leave() {
        if (front == rear) return null;
        front = (front + 1) % queueArray.length;
        return queueArray[front];
    }

    @Override
    public Object peek() {
        if (front == rear) return null;
        return queueArray[(front + 1) & queueArray.length];
    }

    @Override
    public void clear() {
        front = rear = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }
}
