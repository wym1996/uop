package org.jeecg.modules.user_role.service;

import org.jeecg.modules.user_role.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 用户角色模块
 * @Author: hBaby
 * @Date:   2019-09-25
 * @Version: V1.0
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 查询所有的用户角色信息
     * @return
     */
    Map<String,String> queryUserRole();
}
