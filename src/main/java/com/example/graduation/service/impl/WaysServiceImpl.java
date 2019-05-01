package com.example.graduation.service.impl;

import com.example.graduation.entity.*;
import com.example.graduation.mapper.WaysMapper;
import com.example.graduation.response.Response;
import com.example.graduation.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
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
 * @since 2019-03-03
 */
@Service
public class WaysServiceImpl extends ServiceImpl<WaysMapper, Ways> implements WaysService {

    @Autowired
    private LotService lotService;
    @Autowired
    private ParkService parkService;
    @Autowired
    private PathService pathService;
    @Autowired
    private VertexService vertexService;


    @Override
    public void add(Ways ways) {
        insert(ways);
    }

    @Override
    public String out(Record record) {
        Park park = parkService.get(record.getParkid());
        List<Integer> list = new ArrayList();
        Lot lot = lotService.find(record.getCode(), record.getParkid());
        //放入区域周围所有的路口
        if (park.getRightdown() != null) {
            list.add(park.getRightdown());
        }
        if (park.getRightup() != null) {
            list.add(park.getRightup());
        }
        if (park.getLeftdown() != null) {
            list.add(park.getLeftdown());
        }
        if (park.getLeftup() != null) {
            list.add(park.getLeftup());
        }
        int vertex = 0;
        int length = 0;
        List list1 = null;
        //依此找出最短路径进行比较
        for (int i = 0; i < list.size(); i++) {
            //路径以及距离的总集合
            List<List> lists = pathService.shortPath(list.get(i));
            //弟i个路口所有的路径
            List<List> way = lists.get(0);
            //距离
            List lengt = lists.get(1);
            if (i == 0) {
                vertex = list.get(i);
                if ((int) lengt.get(2) < (int) lengt.get(13)) {
                    length = (int) lengt.get(2);
                    list1 = way.get(2);
                } else {
                    length = (int) lengt.get(13);
                    list1 = way.get(13);
                }
            } else {
                if ((int) lengt.get(2) < (int) lengt.get(13)) {
                    if ((int) lengt.get(2) < length) {
                        length = (int) lengt.get(2);
                        vertex = list.get(i);
                        list1 = way.get(2);
                    }
                } else {
                    if ((int) lengt.get(13) < length) {
                        length = (int) lengt.get(13);
                        vertex = list.get(i);
                        list1 = way.get(13);
                    }
                }
            }
//        return selectById(id).getWay();
        }//循环结束，找出 路径最短的 路口到出口
        System.out.println(list1.toString());
        //已经找出 初始路口以及最短路径 vertex: 初始路口  length: 长度   list1  路径
        //开始返回出场路径
        StringBuffer out = new StringBuffer();
        out.append("离场路线:");
        if (list1.size() == 1 && list1.get(0).equals(vertex)) {
            if (lot.getCode() == 0) {
                out.append("离开车位即可出场");
            } else {
                out.append("离开车位");
                if (park.getRightdown() != null && park.getRightdown().equals(vertex)) {
                    out.append("右转");
                    out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米");
                } else if (park.getRightup() != null && park.getRightup().equals(vertex)) {
                    out.append("左转");
                    out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米");
                } else if (park.getLeftup() != null && park.getRightup().equals(vertex)) {
                    out.append("右转");
                    out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米");
                } else if (park.getLeftdown() != null && park.getLeftdown().equals(vertex)) {
                    out.append("左转");
                    out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米");
                }
            }

        } else {
            for (int i = 1; i < list1.size(); i++) {
                int j = Integer.parseInt(list1.get(i).toString());
                int m = Integer.parseInt(list1.get(i - 1).toString());
                Vertex v = vertexService.findV(m);
                Vertex v2 = vertexService.findV(j);
                Path p = pathService.find(j, m);
                int origin = 0;
                if (i == 1) {
                    out.append("离开车位");
                    //最上面一排逻辑不同
                    if (park.getId().equals("A9") || park.getId().equals("A5") || park.getId().equals("A4") || park.getId().equals("A10") || park.getId().equals("B7")) {
                        if (park.getRightdown() != null && park.getRightdown().equals(vertex)) {
                            out.append("左转");

                            out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米->");

                        } else {
                            out.append("右转");
                            out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                        }
                        if (park.getRightdown() != null && park.getRightdown().equals(v.getId())) {
                            origin = v.getLeftver();
                        } else if (park.getLeftdown() != null && park.getLeftdown().equals(v.getId())) {
                            origin = v.getRightver();
                        }
                    } else if (park.getId().equals("A11") || park.getId().equals("A12") ||
                            park.getId().equals("A12") || park.getId().equals("A13") || park.getId().equals("A14") ||
                            park.getId().equals("A15") || park.getId().equals("A16")) {
                        if (park.getRightdown() != null && park.getRightdown() == vertex) {
                            out.append("左转");
                            out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米->");
                        } else if (park.getLeftdown() != null && park.getLeftdown() == vertex) {
                            out.append("左转");
                            out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                        } else if (park.getRightup() != null && park.getRightup() == vertex) {
                            out.append("右转");
                            out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米->");
                        } else {
                            out.append("右转");
                            out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                        }
                        if ((park.getRightdown() != null && park.getRightdown() == v.getId()) || (park.getRightup() != null && park.getRightup() == v.getId())) {
                            origin = v.getLeftver();
                        } else if ((park.getLeftdown() != null && park.getLeftdown() == v.getId()) || (park.getLeftup() != null && park.getLeftup() == v.getId())) {
                            origin = v.getRightver();
                        }

                    } else {
                        if (park.getId().equals("B8")) {
                            if (park.getRightdown() != null && park.getRightdown() == vertex) {
                                if (lot.getCode() == 1) {
                                    out.append("直接驶出车位");
                                } else {
                                    out.append("右转");
                                    out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                                }
                            } else {  //不可能回头，所以不可能是7一下的车位
                                if (park.getRightup() != null && park.getRightup() == vertex) {
                                    if (lot.getCode() == 7) {
                                        out.append("直接驶出车位");
                                    } else {
                                        out.append("右转");
                                        out.append("行驶" + (lot.getCode() - 8) * lot.getWidth() + "米->");
                                    }
                                }
                            }
                            if ((park.getRightdown() != null && park.getRightdown() == v.getId()) || (park.getLeftdown() != null && park.getLeftdown() == v.getId())) {
                                origin = v.getUpver();
                            } else if ((park.getRightup() != null && park.getRightup() == v.getId()) || (park.getLeftup() != null && park.getLeftup() == v.getId())) {
                                origin = v.getDownver();
                            }
                        } else {
                            if (park.getRightdown() != null && park.getRightdown() == vertex) {
                                out.append("右转");
                                out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                            } else if (park.getRightup() != null && park.getRightup() == vertex) {
                                out.append("左转");
                                out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米->");
                            } else if (park.getLeftup() != null && park.getRightup() == vertex) {
                                out.append("右转");
                                out.append("行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米->");
                            } else if (park.getLeftdown() != null && park.getLeftdown() == vertex) {
                                out.append("左转");
                                out.append("行驶" + (lot.getCode() - 1) * lot.getWidth() + "米->");
                            }
                            if ((park.getRightdown() != null && park.getRightdown() == v.getId()) || (park.getLeftdown() != null && park.getLeftdown() == v.getId())) {
                                origin = v.getUpver();
                            } else if ((park.getRightup() != null && park.getRightup() == v.getId()) || (park.getLeftup() != null && park.getLeftup() == v.getId())) {
                                origin = v.getDownver();
                            }
                        }
                    }
                    //修复bug 离开车位到达第一个路口的转向
                    if (v.getUpver() != null && v.getUpver() == j) {
                        if (v.getDownver() != null && v.getDownver() == origin) {
                            out.append("直行");
                        } else if (v.getLeftver() != null && v.getLeftver() == origin) {
                            out.append("左转");
                        } else if (v.getRightver() != null && v.getRightver() == origin) {
                            out.append("右转");
                        }
                    } else if (v.getRightver() != null && v.getRightver() == j) {
                        if (v.getDownver() != null && v.getDownver() == origin) {
                            out.append("右转");
                        } else if (v.getLeftver() != null && v.getLeftver() == origin) {
                            out.append("直行");
                        } else if (v.getUpver() != null && v.getUpver() == origin) {
                            out.append("左转");
                        }
                    } else if (v.getLeftver() != null && v.getLeftver() == j) {
                        if (v.getDownver() != null && v.getDownver() == origin) {
                            out.append("左转");
                        } else if (v.getRightver() != null && v.getRightver() == origin) {
                            out.append("直行");
                        } else if (v.getUpver() != null && v.getUpver() == origin) {
                            out.append("右转");
                        }
                    } else if (v.getDownver() != null && v.getDownver() == j) {
                        if (v.getUpver() != null && v.getUpver() == origin) {
                            out.append("直行1");
                        } else if (v.getRightver() != null && v.getRightver() == origin) {
                            out.append("左转1");
                        } else if (v.getLeftver() != null && v.getLeftver() == origin) {
                            out.append("右转1");
                        }
                    }
                    out.append("行驶" + p.getLengt() + "米 -> ");

//-------------------------------------------------------------------------------------------
                } else {
                    int k = Integer.parseInt(list1.get(i - 2).toString());
                    if (v.getUpver() != null && v.getUpver() == j) {
                        if (v.getDownver() != null && v.getDownver() == k) {
                            out.append("直行");
                        } else if (v.getLeftver() != null && v.getLeftver() == k) {
                            out.append("左转");
                        } else if (v.getRightver() != null && v.getRightver() == k) {
                            out.append("右转");
                        }
                    } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver() == k) {
                            out.append("右转");
                        } else if (v.getLeftver() != null && v.getLeftver() == k) {
                            out.append("直行");
                        } else if (v.getUpver() != null && v.getUpver() == k) {
                            out.append("左转");
                        }
                    } else if (v.getLeftver() != null && v.getLeftver() == j) {
                        if (v.getDownver() != null && v.getDownver() == k) {
                            out.append("左转");
                        } else if (v.getRightver() != null && v.getRightver() == k) {
                            out.append("直行");
                        } else if (v.getUpver() != null && v.getUpver() == k) {
                            out.append("右转");
                        }
                    } else if (v.getDownver() != null && v.getDownver() == j) {
                        if (v.getUpver() != null && v.getUpver() == k) {
                            out.append("直行");
                        } else if (v.getRightver() != null && v.getRightver() == k) {
                            out.append("左转");
                        } else if (v.getLeftver() != null && v.getLeftver() == k) {
                            out.append("右转");
                        }
                    } else if (v.getBlowver() != null && v.getBlowver() == j) {
                        if (v.getLeftver() != null && v.getLeftver() == k) {
                            out.append("直行，走楼梯");
                        } else if (v.getUpver() != null && v.getUpver() == k) {
                            out.append("左转，走楼梯");
                        } else if (v.getDownver() != null && v.getDownver() == k) {
                            out.append("右转，走楼梯");
                        }
                    }
                    out.append("行驶" + p.getLengt() + "米 -> ");
                }
            }
            out.append("到达出口!");
        }
        return out.toString();

    }

    @Override
    public String out(String id) {
        return selectById(id).getWay();
    }

}