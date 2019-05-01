package com.example.graduation.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.graduation.entity.Park;
import com.example.graduation.entity.Path;
import com.example.graduation.mapper.PathMapper;
import com.example.graduation.service.ParkService;
import com.example.graduation.service.PathService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.graduation.util.AjacencyGraph;
import com.example.graduation.util.EdgeElement;
import com.example.graduation.util.ListRe;
import com.example.graduation.util.SequenceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sr
 * @since 2019-01-29
 */
@Service
public class PathServiceImpl extends ServiceImpl<PathMapper, Path> implements PathService {

    @Autowired
    private ParkService parkService;

    @Override
    public List<Path> paths() {
        EntityWrapper<Path> ew = new EntityWrapper<>();
        return selectList(ew);
    }


    //求出所有的最短路径+及路径长度
    @Override
    public List shortPath() {
        int n, t;
        List<Path> list = paths();
        int[][] a = new int[list.size()][3];
        for (int i = 0; i < a.length; i++) {
            a[i][0] = list.get(i).getBegin();
            a[i][1] = list.get(i).getEnd();
            a[i][2] = list.get(i).getLengt();
        }
        //12个路口，无相有权图  --1
        AjacencyGraph ga = new AjacencyGraph(12, 1);
        EdgeElement[] dd = new EdgeElement[a.length];
        for (int i = 0; i < a.length; i++) {
            dd[i] = new EdgeElement(a[i][0], a[i][1], a[i][2]);
        }
        ga.createGraph(dd);
        ga.output();
        int[] dist = new int[ga.vartices()];
        ListRe[] path = new SequenceList[ga.vartices()];
        AjacencyGraph.Dijkstra(ga, 0, dist, path);
        List dists = new ArrayList();
        for (int i = 0; i < dist.length; i++)
            dists.add(dist[i]);
        List<List> list1 = new ArrayList();
        for (int i = 0; i < path.length; i++) {
            /**
             * 跟新后forward返回的是一个路口的顺序线性表arraylist
             */
            list1.add(path[i].forward());
        }

        list1.remove(0);
        /**
         *  用于插入list首
         */
        List list2 = new ArrayList();
        list2.add(0);
        list1.add(0, list2);
        System.out.print(list1);
        List<List> list3 = new ArrayList();
        //路径
        list3.add(list1);
        //距离
        list3.add(dists);
        return list3;
    }
    //求出所有的最短路径+及路径长度,多路口实现
    @Override
    public List shortPath(int inside) {
        int n, t;
        List<Path> list = paths();
        int[][] a = new int[list.size()][3];
        for (int i = 0; i < a.length; i++) {
            a[i][0] = list.get(i).getBegin();
            a[i][1] = list.get(i).getEnd();
            a[i][2] = list.get(i).getLengt();
        }
        //12个路口，无相有权图  --1
        AjacencyGraph ga = new AjacencyGraph(24, 1);
        EdgeElement[] dd = new EdgeElement[a.length];
        for (int i = 0; i < a.length; i++) {
            dd[i] = new EdgeElement(a[i][0], a[i][1], a[i][2]);
        }
        ga.createGraph(dd);
        ga.output();
        int[] dist = new int[ga.vartices()];
        ListRe[] path = new SequenceList[ga.vartices()];
        AjacencyGraph.Dijkstra(ga, inside, dist, path);
        List dists = new ArrayList();
        System.out.println("最短距离:");
        for (int i = 0; i < dist.length; i++) {
            dists.add(dist[i]);
            System.out.println(inside+"-" + i + ":" + dist[i]);
        }
        System.out.println();
        List<List> list1 = new ArrayList();
        for (int i = 0; i < path.length; i++) {
            /**
             * 跟新后forward返回的是一个路口的顺序线性表arraylist
             */
            list1.add(path[i].forward());
           System.out.println();
        }

//        list1.remove(0);
//        /**
//         *  用于插入list首
//         */
        List list2 = new ArrayList();
        list2.add(inside);
//        list2.add(0);
//        list1.add(0, list2);
        list1.set(inside,list2);
        System.out.print(list1);
        List<List> list3 = new ArrayList();
        //路径
        list3.add(list1);
        //距离
        list3.add(dists);
        return list3;
    }

    @Override
    public Path find(int i, int j) {
        return baseMapper.findPath(i, j);
    }

}
