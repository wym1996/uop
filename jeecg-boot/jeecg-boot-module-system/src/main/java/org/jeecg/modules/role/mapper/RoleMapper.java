package org.jeecg.modules.role.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.role.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.users.entity.User;

/**
 * @Description: 角色管理
 * @Author: hBaby
 * @Date:   2019-09-25
 * @Version: V1.0
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过角色名查询用户信息
     * @param role_name
     * @return
     */
    public Role getRoleByName(@Param("role_name") String role_name);
}
