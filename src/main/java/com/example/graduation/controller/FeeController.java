package com.example.graduation.controller;


import com.example.graduation.entity.Fee;
import com.example.graduation.response.Response;
import com.example.graduation.service.FeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-01-22
 */
@Api(tags = "费用管理Api")
@RestController
@RequestMapping("/fee")
public class FeeController {


    @Autowired
    private FeeService feeService;

    @ApiOperation(value = "修改费用")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PutMapping("/update")
    public Response update(@RequestBody Fee fee) {
        feeService.update(fee);
        return Response.success("修改成功");
    }

    @ApiOperation(value = "管理费用参数")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @GetMapping("/list")
    public Response list() {
        Map<Integer, Object> map = new TreeMap<>();
        for (Fee fee : feeService.fees()) {
            map.put(fee.getId(), fee);
        }
        return Response.success().putAllT(map);
    }


}

