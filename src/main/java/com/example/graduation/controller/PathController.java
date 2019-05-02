package com.example.graduation.controller;


import com.example.graduation.entity.*;
import com.example.graduation.response.Response;
import com.example.graduation.result.LotUpdate;
import com.example.graduation.result.Support;
import com.example.graduation.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-01-29
 */
@Api(tags = "导航相关Api")
@RestController
@RequestMapping("/path")
public class PathController {
    @Autowired
    private PathService pathService;
    @Autowired
    private ParkService parkService;
    @Autowired
    private VertexService vertexService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private LotService lotService;

    @ApiOperation(value = "大车入场", notes = "大车入场")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @GetMapping("/big/in")
    public Response status(@RequestParam("id") int id, @RequestParam("car") String car) {
        Record record = new Record();
        record.setId(car);
        //最短路径及长度
        //首先调用最短路径算法，把入口到所有路口的最短路路径求出
        List<List> list = pathService.shortPath(id);
//        System.out.print(list.get(0).toString());
//        System.out.print(list.get(1).toString());
        //找出小车车有空位的区域
        Support support = getBigPathList2(list, parkService, lotService);
        //路径
        List l = support.getPath();
        //距离
        List<Double> l1 = support.getLength();

        List<Park> parks = support.getPark();
        List<Lot> lots = support.getLot();
        List<Park> parkList = parkService.bigList();
        Set set = getSet(parkList);
        List list1 = Arrays.asList(set.toArray());
        Map<String, String> map = new TreeMap<>();
        //key - 路口   value - 距离
        for (int i = 0; i < list1.size(); i++) {
            map.put(Integer.toString((int) list1.get(i)), Double.toString((l1.get((int) list1.get(i)))));
        }
        System.out.println(map.toString());
        /**
         *   排序后的map 第一个元素极为最近区域附近的路口
         */
        Map map1 = sortMapByValue(map);
        List<String> al = new ArrayList<>(map1.keySet());
        /**
         *    找出最近的距离以及路口
         */
        int vertex = Integer.valueOf(al.get(0));
        double length = Double.valueOf(map.get(String.valueOf(vertex)));
//        System.out.println(vertex + ":" + length + " :" + l.get(vertex));
        Park park = parks.get(vertex);
        Lot lot = lots.get(vertex);
        List paths = (List) l.get(vertex);
        record.setParkid(park.getId()).setFloor(park.getFloor());
        //模拟自动检测，尾号为7时模拟进错车位，然后通过车牌号查询所在车位，并与实际车位比较，若不相符，则修改信息。
        boolean flag = false;
        LotUpdate lotUpdate = null;
        StringBuffer stringBuffer = new StringBuffer(car);
        if (stringBuffer.substring(stringBuffer.length() - 1, stringBuffer.length()).equals("7")) {
            flag = true;
            Park park2 = parkService.findBig(7);
            record.setParkid(park2.getId()).setFloor(park.getFloor());
            int code2 = lotService.findEmpty(park2.getId()).getCode();
            record.setCode(code2);
            if (!recordService.in(record)) {
                return Response.fail("请输入正确的车牌号");
            }
            if (record.getParkid() != park.getId() || record.getCode() != lot.getCode()) {
                lotUpdate = new LotUpdate().setCode(lot.getCode()).setCarid(car)
                        .setParkid(park.getId())
                        .setCodeUp(code2)
                        .setParkidUp(park2.getId());
            }
        } else if (!recordService.in(record.setCode(lot.getCode()))) {
            return Response.fail("请输入正确的车牌号");
        }
        lot.setCarid(car).setEmpty("no");
        lotService.in(lot);
        if (flag) {
            lotService.updateLot(lotUpdate);
        }
        return Response.success(getInPath2(paths, vertexService, pathService, park, lot, id));
    }

    @ApiOperation(value = "小车入场", notes = "小车入场")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @GetMapping("/small/in")
    public Response sin(@RequestParam("id") int id, @RequestParam("car") String car) {
        Record record = new Record();
        record.setId(car);
        //最短路径及长度
        //首先调用最短路径算法，把入口到所有路口的最短路路径求出
        List<List> list = pathService.shortPath(id);
        System.out.print(list.get(0).toString());
        System.out.print(list.get(1).toString());

        //找出小车车有空位的区域
        Support support = getSmallPathList2(list, parkService, lotService);
        //路径
        List l = support.getPath();
        //距离
        List<Double> l1 = support.getLength();
        List<Park> parks = support.getPark();
        List<Lot> lots = support.getLot();
        List<Park> parkList = parkService.smallList();
        Set set = getSet(parkList);
        List list1 = Arrays.asList(set.toArray());
        Map<String, String> map = new TreeMap<>();
        //key - 路口   value - 距离
        for (int i = 0; i < list1.size(); i++) {
            map.put(Integer.toString((int) list1.get(i)), Double.toString((l1.get((int) list1.get(i)))));
        }
//        System.out.println(map.toString());
        /**
         *   排序后的map 第一个元素极为最近区域附近的路口
         */
        Map map1 = sortMapByValue(map);
        List<String> al = new ArrayList<>(map1.keySet());
        /**
         *    找出最近的距离以及路口
         */
        int vertex = Integer.valueOf(al.get(0));
        double length = Double.valueOf(map.get(String.valueOf(vertex)));
        System.out.println(vertex + ":" + length + " :" + l.get(vertex));
        Park park = parks.get(vertex);
        Lot lot = lots.get(vertex);
        List paths = (List) l.get(vertex);
        record.setParkid(park.getId()).setFloor(park.getFloor());
        if (!recordService.in(record.setCode(lot.getCode()))) {
            return Response.fail("请输入正确的车牌号");
        }
        lot.setCarid(car).setEmpty("no");
        lotService.in(lot);
        return Response.success(getInPath2(paths, vertexService, pathService, park, lot, id));
    }


    @ApiOperation(value = "多进出口测试", notes = "多进出口测试")
    @GetMapping("/test")
    public Response test(@RequestParam("id") int id, @RequestParam("car") String car) {
        Record record = new Record();
        record.setId(car);
        //最短路径及长度
        //首先调用最短路径算法，把入口到所有路口的最短路路径求出
        List<List> list = pathService.shortPath(id);
//        System.out.print(list.get(0).toString());
//        System.out.print(list.get(1).toString());

        //找出小车车有空位的区域
        Support support = getSmallPathList2(list, parkService, lotService);
        //路径
        List l = support.getPath();
        //距离
        List<Double> l1 = support.getLength();

        List<Park> parks = support.getPark();
        List<Lot> lots = support.getLot();
        List<Park> parkList = parkService.smallList();
        Set set = getSet(parkList);
        List list1 = Arrays.asList(set.toArray());
        Map<String, String> map = new TreeMap<>();
        //key - 路口   value - 距离
        for (int i = 0; i < list1.size(); i++) {
            map.put(Integer.toString((int) list1.get(i)), Double.toString((l1.get((int) list1.get(i)))));
        }
//        System.out.println(map.toString());
        /**
         *   排序后的map 第一个元素极为最近区域附近的路口
         */
        Map map1 = sortMapByValue(map);
        List<String> al = new ArrayList<>(map1.keySet());
        /**
         *    找出最近的距离以及路口
         */
        int vertex = Integer.valueOf(al.get(0));
        double length = Double.valueOf(map.get(String.valueOf(vertex)));
        System.out.println(vertex + ":" + length + " :" + l.get(vertex));
        Park park = parks.get(vertex);
        Lot lot = lots.get(vertex);
        List paths = (List) l.get(vertex);
        record.setParkid(park.getId());
        if (!recordService.in(record.setCode(lot.getCode()))) {
            return Response.fail("请输入正确的车牌号");
        }
        lot.setCarid(car).setEmpty("no");
        lotService.in(lot);
        return Response.success(getInPath2(paths, vertexService, pathService, park, lot, id));
    }

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }


    /**
     * 路线导航 s
     * 如果路线为0，那么进入入口就可以到
     * 否则，将路线分解
     */
    public static String getInPath(List paths, VertexService vertexService, PathService pathService, Park park, Lot lot, int id) {

        StringBuffer s = new StringBuffer();
        s.append("目标区域:" + park.getId() + "-" + lot.getCode() + ",路线:");

        /**
         * 入场导航
         */
        if ((paths.size() == 1 && paths.get(0).equals(id)) || paths.size() == 0) {
            s.append("入口进入");
            if (id == 12) {
                if (park.getRightdown() != null && park.getRightdown() == id) {
                    s.append("右转");
                } else if (park.getRightup() != null && park.getRightup() == id) {
                    s.append("左转");
                }
            } else {
                s.append("直行");
            }
            if (lot.getCode() == park.getTotalcount()) {
                s.append("即可达到");
            } else {
                if ((park.getLeftdown() != null && park.getLeftdown() == Integer.parseInt(paths.get(0).toString())) ||
                        (park.getRightdown() != null && park.getRightdown() == Integer.parseInt(paths.get(0).toString()))) {
                    s.append("沿着区域行驶" + (lot.getCode() - 1) * lot.getWidth() + "米 ->");
                } else {
                    s.append("沿着区域行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米 ->");
                }
            }
            s.append("达到目的地");
        } else {
            s.append("入口进入->");
            for (int i = 1; i < paths.size(); i++) {
                int j = Integer.parseInt(paths.get(i).toString());
                int m = Integer.parseInt(paths.get(i - 1).toString());
                Vertex v = vertexService.findV(m);
                Vertex v2 = vertexService.findV(j);
                Path p = pathService.find(j, m);
                //由于多路口，所以车辆进入的基础方向就不确定，需要多重验证方向。
                //要确定方向，需要知道你从面向哪个方向，到哪个方向，只知道其中一个条件时不行的。
                if (i == 1) {
                    //入口为0时
                    if (id == 0) {
                        if (v.getUpver() != null && v.getUpver().equals(j)) {
                            s.append("直行");
                        } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                            s.append("右转");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(j)) {
                            s.append("左转");
                        } else {
                            s.append("直行");
                        }
                    } else {
                        if (v.getUpver() != null && v.getUpver().equals(j)) {
                            s.append("右转");
                        } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                            s.append("掉头");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(j)) {
                            s.append("直行");
                        } else {
                            s.append("左转");
                        }
                    }
                } else {
                    int k = Integer.parseInt(paths.get(i - 2).toString());
                    if (v.getUpver() != null && v.getUpver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("直行");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(k)) {
                            s.append("左转");
                        } else if (v.getRightver() != null && v.getRightver().equals(k)) {
                            s.append("右转");
                        }
                    } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("右转");
                        }
                        if (v.getLeftver() != null && v.getLeftver().equals(k)) {
                            s.append("直行");
                        }
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("左转");
                        }

                    } else if (v.getLeftver() != null && v.getLeftver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("左转");
                        }
                        if (v.getRightver() != null && v.getRightver().equals(k)) {
                            s.append("直行");
                        }
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("右转");
                        }
                    } else {
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("直行");
                        }
                        if (v.getRightver() != null && v.getRightver().equals(k)) {
                            s.append("右转");
                        }
                        if (v.getLeftver() != null && v.getLeftver().equals(k)) {
                            s.append("左转");
                        }
                    }
                }
                //多入口方向导航结束.
                s.append("行驶" + p.getLengt() + "米 -> ");
                //↓该判断是为了区分是否已到达目的区域方便进行精确车位的导航.
                if (i == paths.size() - 1) {
                    //↓判断为 到达区域所属路口后该如何转向进入具体车位,同样，方向需要多重验证.
                    if (park.getId().equals("A9") || park.getId().equals("A5") || park.getId().equals("A4") || park.getId().equals("A10")) {
                        if ((v2.getRightver() != null && v2.getRightver().equals(m)) || (v2.getLeftver() != null && v.getLeftver().equals(m))) {
                            s.append("直行");
                        } else if (v2.getDownver() != null && v.getDownver().equals(m)) {
                            //因为A9,5,4,10是最上面一层，所以这里的upver可以不考虑
                            if (park.getRightdown() != null && park.getRightdown() == j) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        }
                        //↓该判断为 从路口到具体车位要行驶多少米
                        if (park.getLeftdown() != null && park.getLeftdown() == j) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }
                        //↑end
                    } else {
                        if ((v2.getUpver() != null && v2.getUpver().equals(m)) || (v2.getDownver() != null && v2.getDownver().equals(m))) {
                            s.append("直行");
                        } else if ((v2.getRightver() != null && v2.getRightver().equals(m))) {
                            if (park.getRightdown() != null && park.getRightdown() == j) {
                                s.append("右转");
                            } else if (park.getRightup() != null && park.getRightup() == j) {
                                s.append("左转");
                            }
                        } else {
                            if (park.getLeftdown() != null && park.getLeftdown() == j) {
                                s.append("左转");
                            } else if (park.getLeftup() != null && park.getLeftup() == j) {
                                s.append("右转");
                            }
                        }
                        //↓该判断为 从路口到具体车位要行驶多少米
                        if ((park.getLeftdown() != null && park.getLeftdown() == j) || (park.getRightdown() != null && park.getRightdown() == j)) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }
                    }
                }
                //↑end
            }
            s.append("到达目的地");
        }
        return s.toString();
    }

    public static String getInPath2(List paths, VertexService vertexService, PathService pathService, Park park, Lot lot, int id) {

        StringBuffer s = new StringBuffer();
        s.append("目标区域:" + park.getFloor() + park.getId() + "-" + lot.getCode() + ",路线:");

        /**
         * 入场导航
         */
        if ((paths.size() == 1 && paths.get(0).equals(id)) || paths.size() == 0) {
            s.append("入口进入");
            if (id == 12) {
                if (park.getRightdown() != null && park.getRightdown() == id) {
                    s.append("右转");
                } else if (park.getRightup() != null && park.getRightup() == id) {
                    s.append("左转");
                }
            } else {
                s.append("直行");
            }
            if (lot.getCode() == park.getTotalcount()) {
                s.append("即可达到");
            } else {
                if ((park.getLeftdown() != null && park.getLeftdown() == Integer.parseInt(paths.get(0).toString())) ||
                        (park.getRightdown() != null && park.getRightdown() == Integer.parseInt(paths.get(0).toString()))) {
                    s.append("沿着区域行驶" + (lot.getCode() - 1) * lot.getWidth() + "米 ->");
                } else {
                    s.append("沿着区域行驶" + (park.getTotalcount() - lot.getCode()) * lot.getWidth() + "米 ->");
                }
            }
            s.append("达到目的地");
        } else {
            s.append("入口进入->");
            for (int i = 1; i < paths.size(); i++) {
                int j = Integer.parseInt(paths.get(i).toString());
                int m = Integer.parseInt(paths.get(i - 1).toString());
                Vertex v = vertexService.findV(m);
                Vertex v2 = vertexService.findV(j);
                Path p = pathService.find(j, m);
                //由于多路口，所以车辆进入的基础方向就不确定，需要多重验证方向。
                //要确定方向，需要知道你从面向哪个方向，到哪个方向，只知道其中一个条件时不行的。
                if (i == 1) {
                    //入口为0时
                    if (id == 0) {
                        if (v.getUpver() != null && v.getUpver().equals(j)) {
                            s.append("直行");
                        } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                            s.append("右转");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(j)) {
                            s.append("左转");
                        } else {
                            s.append("直行");
                        }
                    } else {
                        if (v.getUpver() != null && v.getUpver().equals(j)) {
                            s.append("右转");
                        } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                            s.append("掉头");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(j)) {
                            s.append("直行");
                        } else if (v.getAbover() != null && v.getAbover().equals(j)) {
                            s.append("从楼梯进入二层区域");
                        } else {
                            s.append("左转");
                        }
                    }
                } else {
                    int k = Integer.parseInt(paths.get(i - 2).toString());
                    if (v.getUpver() != null && v.getUpver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("直行");
                        } else if (v.getLeftver() != null && v.getLeftver().equals(k)) {
                            s.append("左转");
                        } else if ((v.getRightver() != null && v.getRightver().equals(k)) || (v.getDownver() != null && v.getBlowver().equals(k))) {
                            //到二层后判断
                            s.append("右转");
                        }
                    } else if (v.getRightver() != null && v.getRightver().equals(j)) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("右转");
                        }
                        if (v.getLeftver() != null && v.getLeftver().equals(k)) {
                            s.append("直行");
                        }
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("左转");
                        }

                    } else if ((v.getLeftver() != null && v.getLeftver().equals(j))) {
                        if (v.getDownver() != null && v.getDownver().equals(k)) {
                            s.append("左转");
                        }
                        if ((v.getRightver() != null && v.getRightver().equals(k)) || (v.getDownver() != null && v.getBlowver().equals(k))) {
                            //到二层后判断
                            s.append("直行");
                        }
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("右转");
                        }
                    } else {
                        if (v.getUpver() != null && v.getUpver().equals(k)) {
                            s.append("直行");
                        }
                        if (v.getRightver() != null && v.getRightver().equals(k)) {
                            s.append("右转");
                        }
                        if ((v.getLeftver() != null && v.getLeftver().equals(k)) || (v.getDownver() != null && v.getBlowver().equals(k))) {
                            //到二层后判断
                            s.append("左转");
                        }
                    }
                }
                //多入口方向导航结束.
                s.append("行驶" + p.getLengt() + "米 -> ");
                //↓该判断是为了区分是否已到达目的区域方便进行精确车位的导航.
                if (i == paths.size() - 1) {
                    //↓判断为 到达区域所属路口后该如何转向进入具体车位,同样，方向需要多重验证.
                    if (park.getId().equals("A9") || park.getId().equals("A5") || park.getId().equals("A4") || park.getId().equals("A10") || park.getId().equals("B7")) {
                        if (v2.getRightver() != null && v2.getRightver().equals(m) || (v2.getLeftver() != null && v.getLeftver() == m) | (v2.getBlowver() != null && v2.getBlowver() == m)) {
                            s.append("直行");
                        } else if (v2.getDownver() != null && v.getDownver().equals(m)) {
                            //因为A9,5,4,10是最上面一层，所以这里的upver可以不考虑
                            if (park.getRightdown() != null && park.getRightdown() == j) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        }//转向
                        //↓该判断为 从路口到具体车位要行驶多少米
                        if (park.getLeftdown() != null && park.getLeftdown() == j) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }
                        //↑end
                    } else if ( park.getId().equals("A12") || park.getId().equals("A13") || park.getId().equals("A14") ||
                            park.getId().equals("A15") || park.getId().equals("A16")) {
                        if ((v2.getRightver() != null && v2.getRightver().equals(m)) || (v2.getLeftver() != null && v.getLeftver().equals(m)) || (v2.getBlowver() != null && v.getBlowver().equals(m))) {
                            s.append("直行");
                        } else if (v2.getDownver() != null && v.getDownver().equals(m)) {
                            if ((park.getRightdown() != null && park.getRightdown() == j) || (park.getRightup() != null && park.getRightup() == j)) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        } else {
                            if ((park.getLeftdown() != null && park.getLeftdown().equals(m)) || ((park.getLeftup() != null && park.getLeftup().equals(m)))) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        }
                        //↓该判断为 从路口到具体车位要行驶多少米
                        if ((park.getLeftdown() != null && park.getLeftdown() == j) || (park.getLeftup() != null && park.getLeftup() == j)) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }//↑end
                    }else if (park.getId().equals("A11") ){
                        if ((v2.getRightver() != null && v2.getRightver().equals(m)) || (v2.getLeftver() != null && v.getLeftver().equals(m))) {
                            s.append("直行");
                        } else if (v2.getDownver() != null && v.getDownver().equals(m)) {
                            if ((park.getRightdown() != null && park.getRightdown() == j) || (park.getRightup() != null && park.getRightup() == j)) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        }else if (v2.getBlowver()!=null && v2.getBlowver() == m){
                            s.append("直行");
                        } else {
                            if ((park.getLeftdown() != null && park.getLeftdown().equals(m)) || ((park.getLeftup() != null && park.getLeftup().equals(m)))) {
                                s.append("左转");
                            } else {
                                s.append("右转");
                            }
                        }
                        //↓该判断为 从路口到具体车位要行驶多少米
                        if ((park.getLeftdown() != null && park.getLeftdown() == j) || (park.getLeftup() != null && park.getLeftup() == j)) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }//↑end


                    }

                    else if (park.getId().equals("B8")) {
                        if (park.getRightup() != null && park.getRightup() == j) {
                            if (lot.getCode() > 7) {
                                s.append("右转");
                            } else if (lot.getCode() == 7) {
                                s.append(" ");
                            } else {
                                s.append("左转");
                            }
                        } else {
                            s.append(" ");
                        }
                        if (park.getId().equals("B8")) {  //特殊区域，单独处理
                            if (park.getRightup() != null && park.getRightup() == j) {
                                double len;
                                if (lot.getCode() > 7) {
                                    len = (lot.getCode() - 8) * lot.getWidth();
                                    s.append("沿着区域行驶" + len + "米");
                                } else if (lot.getCode() == 7) {
                                    s.append("直达");
                                } else {
                                    len = (7 - lot.getCode()) * lot.getWidth();
                                    s.append("沿着区域行驶" + len + "米");
                                }
                            } else {
                                s.append("直达");
                            }
                        }
                    } else {
                        if ((v2.getUpver() != null && v2.getUpver().equals(m)) || (v2.getDownver() != null && v2.getDownver().equals(m))) {
                            s.append("直行");
                        } else if ((v2.getRightver() != null && v2.getRightver().equals(m))) {
                            if (park.getRightdown() != null && park.getRightdown() == j) {
                                s.append("右转");
                            } else if (park.getRightup() != null && park.getRightup() == j) {
                                s.append("左转");
                            }
                        } else {
                            if (park.getLeftdown() != null && park.getLeftdown() == j) {
                                s.append("左转");
                            } else if (park.getLeftup() != null && park.getLeftup() == j) {
                                s.append("右转");
                            }
                        }//↓该判断为 从路口到具体车位要行驶多少米
                        if ((park.getLeftdown() != null && park.getLeftdown() == j) || (park.getRightdown() != null && park.getRightdown() == j)) {
                            if (lot.getCode() != 1) {
                                double len;
                                len = (lot.getCode() - 1) * lot.getWidth();
                                s.append("沿着区域行驶" + len + "米");
                            }
                        } else {
                            if (lot.getCode() != park.getTotalcount()) {
                                double len;
                                len = (park.getTotalcount() - lot.getCode()) * lot.getWidth();
                                s.append(("沿着区域行驶") + len + "米");
                            }
                        }

                    }
                }
                //↑end
            }
            s.append("到达目的地");
        }
        return s.toString();
    }

    public static Set getSet(List<Park> parkList) {
        Set set = new HashSet();
        for (Park park : parkList) {
            if (park.getLeftdown() != null) {
                set.add(park.getLeftdown());
            }
            if (park.getRightdown() != null) {
                set.add(park.getRightdown());
            }
            if (park.getLeftup() != null) {
                set.add(park.getLeftup());
            }
            if (park.getRightup() != null) {
                set.add(park.getRightup());
            }
        }
        return set;
    }


    public static Support getSmallPathList(List<List> lists, ParkService parkService, LotService lotService) {
        Support support = new Support();
        //路径
        List l = lists.get(0);
        //距离
        List<Integer> l2 = lists.get(1);
        List<Double> l1 = new ArrayList<>();
        for (int d : l2) {
            l1.add((double) d);
        }
        List<Park> parkList = new ArrayList<>();
        List<Lot> lotList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {   //总循环 ，找出每个路径的 最短距离的 park ,lot ,length.
            parkList.add(null);
            lotList.add(null);
            List<Park> parks = parkService.findSmallList(i);
            Park parkest = null;
            Lot lotest = null;
            double shortest = 0;
            if (parks.size() != 0) {
                for (int j = 0; j < parks.size(); j++) {  // 每次循环，当前park最短距离的车位
                    Park p = parks.get(j);
                    double length = 0; //当前park的最短距离
                    Lot lot = null;  //当前park的最短距离的车位
                    List<Lot> lots = lotService.findEmptyList(p.getId());
                    for (int k = 0; k < lots.size(); k++) {  //每次循环 ，找出最近的lot车位并算出距离;
                        Lot lt = lots.get(k);
                        double len = 0;
                        if (p.getId().equals("A9") || p.getId().equals("A5") || p.getId().equals("A4") || p.getId().equals("A10")) {
                            if (p.getLeftdown() != null && p.getLeftdown() == i) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else {
                            if ((p.getLeftdown() != null && p.getLeftdown() == i) || (p.getRightdown() != null && p.getRightdown() == i)) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        }
                    } //每次循环 ，找出最近的lot车位并算出距离;
                    if (j == 0 || shortest > length) {
                        parkest = p;
                        lotest = lot;
                        shortest = length;

                    }
                }//每次循环，当前park最短距离的车位
            }
            parkList.set(i, parkest);
            lotList.set(i, lotest);
            l1.set(i, l1.get(i) + shortest);
        }

        return support.setLength(l1).setLot(lotList).setPark(parkList).setPath(l);
    }

    public static Support getBigPathList(List<List> lists, ParkService parkService, LotService lotService) {
        Support support = new Support();
        //路径
        List l = lists.get(0);
        //距离
        List<Integer> l2 = lists.get(1);
        List<Double> l1 = new ArrayList<>();
        for (int d : l2) {
            l1.add((double) d);
        }
        List<Park> parkList = new ArrayList<>();
        List<Lot> lotList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {   //总循环 ，找出每个路径的 最短距离的 park ,lot ,length.
            parkList.add(null);
            lotList.add(null);
            List<Park> parks = parkService.findBigList(i);
            Park parkest = null;
            Lot lotest = null;
            double shortest = 0;
            if (parks.size() != 0) {
                for (int j = 0; j < parks.size(); j++) {  // 每次循环，当前park最短距离的车位
                    Park p = parks.get(j);
                    double length = 0; //当前park的最短距离
                    Lot lot = null;  //当前park的最短距离的车位
                    List<Lot> lots = lotService.findEmptyList(p.getId());
                    for (int k = 0; k < lots.size(); k++) {  //每次循环 ，找出最近的lot车位并算出距离;
                        Lot lt = lots.get(k);
                        double len = 0;
                        if (p.getId().equals("A9") || p.getId().equals("A5") || p.getId().equals("A4") || p.getId().equals("A10")) {
                            if (p.getLeftdown() != null && p.getLeftdown() == i) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                        } else {
                            if ((p.getLeftdown() != null && p.getLeftdown() == i) || (p.getRightdown() != null && p.getRightdown() == i)) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        }
                    } //每次循环 ，找出最近的lot车位并算出距离;
                    if (j == 0 || shortest > length) {
                        parkest = p;
                        lotest = lot;
                        shortest = length;

                    }
                }//每次循环，当前park最短距离的车位
            }
            parkList.set(i, parkest);
            lotList.set(i, lotest);
            l1.set(i, l1.get(i) + shortest);
        }

        return support.setLength(l1).setLot(lotList).setPark(parkList).setPath(l);
    }

    public static Support getSmallPathList2(List<List> lists, ParkService parkService, LotService lotService) {
        Support support = new Support();
        //路径
        List l = lists.get(0);
        //距离
        List<Integer> l2 = lists.get(1);
        List<Double> l1 = new ArrayList<>();
        for (int d : l2) {
            l1.add((double) d);
        }
        List<Park> parkList = new ArrayList<>();
        List<Lot> lotList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {   //总循环 ，找出每个路径的 最短距离的 park ,lot ,length.
            parkList.add(null);
            lotList.add(null);
            List<Park> parks = parkService.findSmallList(i);
            Park parkest = null;
            Lot lotest = null;
            double shortest = 0;
            if (parks.size() != 0) {
                for (int j = 0; j < parks.size(); j++) {  // 每次循环，当前park最短距离的车位
                    Park p = parks.get(j);
                    double length = 0; //当前park的最短距离
                    Lot lot = null;  //当前park的最短距离的车位
                    List<Lot> lots = lotService.findEmptyList(p.getId());
                    for (int k = 0; k < lots.size(); k++) {  //每次循环 ，找出最近的lot车位并算出距离;
                        Lot lt = lots.get(k);
                        double len = 0;
                        if (p.getId().equals("A9") || p.getId().equals("A5") || p.getId().equals("A4") || p.getId().equals("A10") || p.getId().equals("B7")) {
                            if (p.getLeftdown() != null && p.getLeftdown() == i) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else if (p.getId().equals("A13") || p.getId().equals("A14") ||
                                p.getId().equals("A15") || p.getId().equals("A16") ||
                                p.getId().equals("A11") || p.getId().equals("A12")) {
                            if ((p.getRightdown() != null && p.getRightdown() == i) || (p.getRightup() != null && p.getRightup() == i)) {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            } else {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            }//↑end
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else if (p.getId().equals("B8")) {
                            if (p.getLeftup() != null && p.getLeftup() == i) {
                                if (lt.getCode() < 3) {
                                    len = (2 - lt.getCode()) * lt.getWidth();
                                } else if (lt.getCode() == 3) {
                                    len = 0;
                                } else {
                                    len = (lt.getCode() - 3) * lt.getWidth();
                                }
                            } else {
                                len = 0;
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else {
                            if ((p.getLeftdown() != null && p.getLeftdown() == i) || (p.getRightdown() != null && p.getRightdown() == i)) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        }
                    } //每次循环 ，找出最近的lot车位并算出距离;
                    if (j == 0 || shortest > length) {
                        parkest = p;
                        lotest = lot;
                        shortest = length;

                    }
                }//每次循环，当前park最短距离的车位
            }
            parkList.set(i, parkest);
            lotList.set(i, lotest);
            l1.set(i, l1.get(i) + shortest);
        }

        return support.setLength(l1).setLot(lotList).setPark(parkList).setPath(l);
    }

    public static Support getBigPathList2(List<List> lists, ParkService parkService, LotService lotService) {
        Support support = new Support();
        //路径
        List l = lists.get(0);
        //距离
        List<Integer> l2 = lists.get(1);
        List<Double> l1 = new ArrayList<>();
        for (int d : l2) {
            l1.add((double) d);
        }
        List<Park> parkList = new ArrayList<>();
        List<Lot> lotList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {   //总循环 ，找出每个路径的 最短距离的 park ,lot ,length.
            parkList.add(null);
            lotList.add(null);
            List<Park> parks = parkService.findBigList(i);
            Park parkest = null;
            Lot lotest = null;
            double shortest = 0;
            if (parks.size() != 0) {
                for (int j = 0; j < parks.size(); j++) {  // 每次循环，当前park最短距离的车位
                    Park p = parks.get(j);
                    double length = 0; //当前park的最短距离
                    Lot lot = null;  //当前park的最短距离的车位
                    List<Lot> lots = lotService.findEmptyList(p.getId());
                    for (int k = 0; k < lots.size(); k++) {  //每次循环 ，找出最近的lot车位并算出距离;
                        Lot lt = lots.get(k);
                        double len = 0;
                        if (p.getId().equals("A9") || p.getId().equals("A5") || p.getId().equals("A4") || p.getId().equals("A10") || p.getId().equals("B7")) {
                            if (p.getLeftdown() != null && p.getLeftdown() == i) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else if (p.getId().equals("A11") || p.getId().equals("A12") ||
                                p.getId().equals("A12") || p.getId().equals("A13") || p.getId().equals("A14") ||
                                p.getId().equals("A15") || p.getId().equals("A16")) {
                            if ((p.getLeftdown() != null && p.getLeftdown() == j) || (p.getLeftup() != null && p.getLeftup() == j)) {
                                if (lt.getCode() != 1) {
                                    len = (lot.getCode() - 1) * lt.getWidth();
                                }
                            } else {
                                if (lt.getCode() != p.getTotalcount()) {
                                    len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                                }
                            }//↑end
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else if (p.getId().equals("B8")) {
                            if (p.getLeftup() != null && p.getLeftup() == j) {
                                if (lt.getCode() < 3) {
                                    len = (2 - lt.getCode()) * lt.getWidth();
                                } else if (lt.getCode() == 3) {
                                    len = 0;
                                } else {
                                    len = (lt.getCode() - 3) * lt.getWidth();
                                }
                            } else {
                                len = 0;
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        } else {
                            if ((p.getLeftdown() != null && p.getLeftdown() == i) || (p.getRightdown() != null && p.getRightdown() == i)) {
                                len = (lt.getCode() - 1) * lt.getWidth();
                            } else {
                                len = (p.getTotalcount() - lt.getCode()) * lt.getWidth();
                            }
                            if (k == 0 || length > len) {
                                length = len;
                                lot = lt;
                            }
                        }
                    } //每次循环 ，找出最近的lot车位并算出距离;
                    if (j == 0 || shortest > length) {
                        parkest = p;
                        lotest = lot;
                        shortest = length;

                    }
                }//每次循环，当前park最短距离的车位
            }
            parkList.set(i, parkest);
            lotList.set(i, lotest);
            l1.set(i, l1.get(i) + shortest);
        }

        return support.setLength(l1).setLot(lotList).setPark(parkList).setPath(l);
    }

}

class MapValueComparator implements Comparator<Map.Entry<String, String>> {

    @Override
    public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {
        return Double.valueOf(me1.getValue()).compareTo(Double.valueOf(me2.getValue()));
    }
}

