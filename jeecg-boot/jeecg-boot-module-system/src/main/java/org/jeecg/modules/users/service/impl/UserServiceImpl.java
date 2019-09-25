package org.jeecg.modules.users.service.impl;

import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.mapper.UserMapper;
import org.jeecg.modules.users.service.IUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
