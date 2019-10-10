package org.jeecg.modules.users.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther wym
 * @description 登录
 */
@RestController
@RequestMapping("")
@Api(tags="用户登录")
@Slf4j
public class UserLoginController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    //使用用户名和密码登录
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ApiOperation("登录")
    public Result<JSONObject> userLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

       //1. 校验用户是否有效
        //调用User实体
        User user=userService.getUserByName(username);
        result=userService.checkUser(user);
       // System.out.println(result); //Result(success=true, message=操作成功！, code=0, result=null, timestamp=1569727762816)
        if(!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
       // String userpassword = PasswordUtil.encrypt(username, password,user.getSalt());
       String syspassword = user.getPassword();
        if (!syspassword.equals(password)) {
            result.error500("用户名或密码错误");
            return result;
        }

//        //用户登录信息
        userInfo(user, result);

        return result;
    }


    /**
     * 用户信息
     *
     * @param User
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(User user, Result<JSONObject> result) {
        String syspassword = user.getPassword();//user实体类
        String username = user.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
//
//        // 获取用户部门信息
         JSONObject obj = new JSONObject();
//        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
//        obj.put("departs", departs);
//        if (departs == null || departs.size() == 0) {
//            obj.put("multi_depart", 0);
//        } else if (departs.size() == 1) {
//            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
//            obj.put("multi_depart", 1);
//        } else {
//            obj.put("multi_depart", 2);
//        }
        obj.put("token", token);
        obj.put("userInfo", user);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

}
