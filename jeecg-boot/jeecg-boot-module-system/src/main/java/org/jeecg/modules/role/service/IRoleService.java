package org.jeecg.modules.role.service;

import org.jeecg.modules.role.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 角色管理
 * @Author: hBaby
 * @Date:   2019-09-25
 * @Version: V1.0
 */
public interface IRoleService extends IService<Role> {

    public Role getRoleByName(String role_name);
}
