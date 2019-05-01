package com.example.graduation.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Record;
import com.example.graduation.response.Response;
import com.example.graduation.result.CarOutResult;
import com.example.graduation.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
@Api(tags = "车辆进出场Api")
@RestController
@RequestMapping("/record")
public class RecordController extends BaseController {

    @Autowired
    private RecordService recordService;


    @ApiOperation(value = "车辆进场", notes = "车辆进场")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PostMapping("/in")
    public Response in(@RequestBody Record record) {
        recordService.in(record);
        return Response.success();
    }

    @ApiOperation(value = "车辆离场", notes = "离场", response = CarOutResult.class)
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @GetMapping("/out")
    public Response out(@RequestParam String id) {
        return Response.success(recordService.out(id));
    }

    @ApiOperation(value = "分页查询车辆车位适用情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header"),
            @ApiImplicitParam(name = "page", value = "分页当前页,不传默认第一页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页一页多少条记录,不传默认12条", dataType = "String", paramType = "query"),
    })
    @GetMapping("/list")
    public Response list() {
        Integer i[] = getPageSizeFromGetRequest();
        Page<Record> page = new Page<>(i[0], i[1]);
        return recordService.list(page);
    }
}

