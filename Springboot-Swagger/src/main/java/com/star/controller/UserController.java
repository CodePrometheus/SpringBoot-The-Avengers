package com.star.controller;

import com.star.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
//用来指定接口的描述文字，用在类上
@Api(tags = "用户处理业务")
public class UserController {

    @GetMapping("find")
    @ApiOperation(value = "查询用户方法",
            notes = "<span style='color:red;'>描述</span>&nbsp;火影村街道办")
    public Map<String, Object> find() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", "查询成功");
        map.put("state", true);
        return map;
    }

    //    @PostMapping("save/{id}/{name}")
    @PostMapping("save")
//    接口说明
    @ApiOperation(value = "查询用户方法",
            notes = "<span style='color:blue;'>描述</span>&nbsp;火影村火影大人")
//    接口参数的说明
/*
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", defaultValue = "21", paramType = "boby"),
            @ApiImplicitParam(name = "name", value = "用户姓名", dataType = "String", defaultValue = "鸣人", paramType = "boby")
    })
*/
    @ApiResponses({
            @ApiResponse(code = 401, message = "路径不存在"),
            @ApiResponse(code = 200, message = "成功")
    })
    public Map<String, Object> save(@RequestBody User user) {
        System.out.println("id = " + user.getId());
        System.out.println("name = " + user.getName());
        Map<String, Object> map = new HashMap<>();
        map.put("success", "查询成功");
        map.put("state", true);
        return map;
    }


}
