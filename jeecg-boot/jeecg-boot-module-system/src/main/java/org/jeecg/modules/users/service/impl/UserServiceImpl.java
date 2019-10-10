package org.jeecg.modules.users.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.mapper.UserMapper;
import org.jeecg.modules.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }
    @Override
    public Result<?> checkUser(User user) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (user == null) {
            result.error500("该用户不存在，请注册");
            //sysBaseAPI.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销,数据库字段status和del_flag
//        if (CommonConstant.DEL_FLAG_1.toString().equals(user.getDelFlag())) {
//            //sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
//            result.error500("该用户已注销");
//            return result;
//        }
        //情况3：根据用户信息查询，该用户已冻结
//        if (CommonConstant.USER_FREEZE.equals(user.getStatus())) {
//           // sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
//            result.error500("该用户已冻结");
//            return result;
//        }
        return result;
    }

   /* @Override
    public String getPasswordByUsername(String username){
       return userMapper.getPasswordByUsername(username);
     }*/

}
