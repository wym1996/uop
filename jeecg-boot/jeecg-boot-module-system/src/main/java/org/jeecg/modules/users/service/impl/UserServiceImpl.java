package org.jeecg.modules.users.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.user_role.entity.UserRole;
import org.jeecg.modules.user_role.mapper.UserRoleMapper;
import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.mapper.UserMapper;
import org.jeecg.modules.users.service.IUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public void addUserWithRole(User user, String roles) {
        this.save(user);
        if(oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                UserRole userRole = new UserRole(user.getId(), roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    public void editUserWithRole(User user, String roles) {
        this.updateById(user);
        //先删后加
        userRoleMapper.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, user.getId()));
        if(oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                UserRole userRole = new UserRole(user.getId(), roleId);
                System.out.println(user.getId());
                userRoleMapper.insert(userRole);
            }
        }
    }
}
