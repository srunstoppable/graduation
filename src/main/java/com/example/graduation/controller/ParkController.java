package com.example.graduation.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Park;
import com.example.graduation.response.Response;
import com.example.graduation.service.ParkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-01-21
 */
@Api(tags = "车位管理api")
@RestController
@RequestMapping("/park")
@RequiresAuthentication
public class ParkController extends BaseController {

    @Autowired
    private ParkService parkService;


    @ApiOperation(value = "分页查询所有停车位信息", notes = "分页查询所有停车位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header"),
            @ApiImplicitParam(name = "page", value = "分页当前页,不传默认第一页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页一页多少条记录,不传默认12条", dataType = "String", paramType = "query"),
    })
    @GetMapping("/list")
    public Response list() {
        Integer[] pageInfo = getPageSizeFromGetRequest();
        Page<Park> page = new Page<>(pageInfo[0], pageInfo[1]);
        return parkService.parks(page);
    }

    @ApiOperation(value = "查询车位使用情况", notes = "查询车位使用情况")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @GetMapping("/status")
    public Response parkStatus() {
        return parkService.parkStatus();
    }


    @ApiOperation(value = "更新车位信息", notes = "更新车位信息")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PutMapping("/update")
    public Response update(@RequestBody Park park) {
       return  parkService.update(park);
    }

    @ApiOperation(value = "检查是否有大车空位")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PostMapping("big")
    public boolean big() {
        return parkService.bigIsLeft();
    }


    @ApiOperation(value = "检查是否有小车空位", notes = "检查是否有小车空位")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PostMapping("small")
    public boolean small() {
        return parkService.smallIsLeft();
    }


}

