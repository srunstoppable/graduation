package com.example.graduation.controller;


import com.example.graduation.response.Response;
import com.example.graduation.service.WaysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-03-03
 */
@Api(tags = "车辆离场路线导航")
@RestController
@RequestMapping("/ways")
public class WaysController {

    @Autowired
    private WaysService waysService;

//    @ApiOperation(value = "离场路线")
//    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
//    @GetMapping("/update")
//    public Response out(@RequestParam String id){
//        return waysService.out(id);
//    }
}

