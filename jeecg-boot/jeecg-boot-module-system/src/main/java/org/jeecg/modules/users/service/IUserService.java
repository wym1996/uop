package org.jeecg.modules.users.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.users.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
public interface IUserService extends IService<User> {
    /**
     * @Author wym
     * @description 通过用户名查找用户
     * @param username
     * @return
     */
    public User getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     * @param user
     * @param roles
     */
    public void addUserWithRole(User user, String roles);

    /**
     * 修改用户和用户角色关系
     * @param user
     * @param roles
     */
    public void editUserWithRole(User user, String roles);

    /**
     * 校验用户是否有效
     * @param
     * @return
     */
    Result checkUser(User user);
    /**
     * 通过用户名查询用户密码
     */
   // String getPasswordByUsername(String username);
}