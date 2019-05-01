package com.example.graduation.util;

/**
 * @author s r
 * @date 2019/1/29
 */
public interface Graph {
    boolean createGraph(EdgeElement[]d);
    int graphType();
    int vartices();
    int edges();
    boolean find(int i,int j);
    boolean putEdge(EdgeElement edgeElement);
    boolean removeEdge(int i,int j);
    int degree(int i);
    int inDegree(int i);
    int outDegree(int i);
    void output();
    void depthFirstSearch(int v);
    void breadthFirstSearch(int v);
    void clearGraph();
}
