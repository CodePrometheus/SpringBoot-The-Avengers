package com.star.swagger.controller;

import com.star.swagger.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
/** 用来指定接口的描述文字，用在类上 说明该类的作用，可以在UI界面上看到的注解*/
@Api(tags = "用户处理业务")
public class UserController {


    /**
     * @return
     * @ApiOperation：用在请求的方法上，说明方法的用途、作用 value="说明方法的用途、作用"
     * notes="方法的备注说明"
     */
    @GetMapping("find")
    @ApiOperation(value = "查询用户方法",
            notes = "<span style='color:red;'>描述</span>&nbsp;火影村街道办")
    public Map<String, Object> find() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", "查询成功");
        map.put("state", true);
        return map;
    }

    // @PostMapping("save/{id}/{name}")
    @PostMapping("save")
    // 接口说明
    @ApiOperation(value = "查询用户方法",
            notes = "<span style='color:blue;'>描述</span>&nbsp;火影村火影大人")
/**
 *  接口参数的说明
 * @ApiImplicitParams：用在请求的方法上，表示一组参数说明
 *     @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
 *         name：参数名
 *         value：参数的汉字说明、解释
 *         required：参数是否必须传
 *         paramType：参数放在哪个地方
 *             · header --> 请求参数的获取：@RequestHeader
 *             · query --> 请求参数的获取：@RequestParam
 *             · path（用于restful接口）--> 请求参数的获取：@PathVariable
 *             · div（不常用）
 *             · form（不常用）
 *         dataType：参数类型，默认String，其它值dataType="Integer"
 *         defaultValue：参数的默认值
 *
 @ApiImplicitParams({
 @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", defaultValue = "21", paramType = "boby"),
 @ApiImplicitParam(name = "name", value = "用户姓名", dataType = "String", defaultValue = "鸣人", paramType = "boby")
 })
 */

    /**
     * @ApiResponses：用在请求的方法上，表示一组响应
     *     @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
     *         code：数字，例如400
     *         message：信息，例如"请求参数没填好"
     *         response：抛出异常的类
     *
     *  @ApiModel：用于响应类上，表示一个返回响应数据的信息
     *             （这种一般用在post创建的时候，使用@RequestBody这样的场景，
     *             请求参数无法使用@ApiImplicitParam注解进行描述的时候）
     *     @ApiModelProperty：用在属性上，描述响应类的属性
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
