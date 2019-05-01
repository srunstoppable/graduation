package com.example.graduation.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.example.graduation.entity.Lot;
import com.example.graduation.entity.Park;
import com.example.graduation.response.Response;
import com.example.graduation.result.LotUpdate;
import com.example.graduation.service.LotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sr
 * @since 2019-02-20
 */
@Api(tags = "详细车位api")
@RestController
@RequestMapping("/lot")
public class LotController extends BaseController {

    @Autowired
    private LotService lotService;

    @ApiOperation(value = "查询所有车位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header"),
            @ApiImplicitParam(name = "page", value = "分页当前页,不传默认第一页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页一页多少条记录,不传默认12条", dataType = "String", paramType = "query"),
    })
    @GetMapping("/list")
    public Response list() {
        Integer[] pageInfo = getPageSizeFromGetRequest();
        Page<Lot> page = new Page<>(pageInfo[0], pageInfo[1]);
        return lotService.lotes(page);
    }

    @ApiOperation(value = "更新车位信息", notes = "更新车位信息")
    @ApiImplicitParam(name = "Authorization", value = "Authorization", required = true, paramType = "header")
    @PutMapping("/update")
    public Response update(@RequestBody LotUpdate lotUpdate){
        return lotService.updateLot(lotUpdate);

    }


}

