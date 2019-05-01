//package com.example.graduation.util;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @author s r
// * @date 2019/1/29
// */
//public class Test {
//    public static void main(String args[]){
//        int n,t;
//        AjacencyGraph ga=new AjacencyGraph(5,1);
//        //1为无向 带权
//        int [][]a= {{0,1,2},{0,2,3},{0,3,8},{1,3,12},{2,3,6},{2,4,1},{3,4,4}};
//        EdgeElement[]dd=new EdgeElement[a.length];
//        for (int i=0 ;i<a.length;i++)
//            dd[i] =new EdgeElement(a[i][0],a[i][1],a[i][2]);
////        if(ga.createGraph(dd))
////            System.out.println("成功");
////        ga.output();
//        ga.createGraph(dd);
//        int []dist=new int[ga.vartices()];
//        List list=new LinkedList();
//        List[] path=new List[ga.vartices()];
//        AjacencyGraph.Dijkstra(ga,0,dist,path);
//        System.out.println("最短距离:");
//        for(int i=0;i<dist.length;i++)
//            System.out.println("0-"+i+":"+dist[i]);
//        System.out.println();
//
//        for (int i=0;i<path.length;i++){
//            System.out.println(path[i]);
//        }
//    }
//
//
//}
