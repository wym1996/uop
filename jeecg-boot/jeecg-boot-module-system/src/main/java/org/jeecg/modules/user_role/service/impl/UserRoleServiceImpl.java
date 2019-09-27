package org.jeecg.modules.user_role.service.impl;

import org.jeecg.modules.role.entity.Role;
import org.jeecg.modules.role.service.IRoleService;

import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.user_role.entity.UserRole;
import org.jeecg.modules.user_role.mapper.UserRoleMapper;
import org.jeecg.modules.user_role.service.IUserRoleService;
import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.service.IUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户角色模块
 * @Author: jeecg-boot
 * @Date:   2019-09-25
 * @Version: V1.0
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Resource
    private IUserService userService;
    @Resource
    private IRoleService roleService;

    /**
     * 查询所有用户对应的角色信息
     */
    @Override
    public Map<String, String> queryUserRole() {
        List<UserRole> uRoleList = this.list();
        List<User> userList = userService.list();
        List<Role> roleList = roleService.list();
        Map<String,String> map = new IdentityHashMap<>();
        String userId = "";
        String roleId = "";
        String roleName = "";
        if(uRoleList != null && uRoleList.size() > 0) {
            for(UserRole uRole : uRoleList) {
                roleId = uRole.getRoleId();
                for(User user : userList) {
                    userId = user.getId();
                    if(uRole.getUserId().equals(userId)) {
                        roleName = this.searchByRoleId(roleList,roleId);
                        map.put(userId, roleName);
                    }
                }
            }
            return map;
        }
        return map;
    }

    /**
     * queryUserRole调用的方法
     * @param roleList
     * @param roleId
     * @return
     */
    private String searchByRoleId(List<Role> roleList, String roleId) {
        while(true) {
            for(Role role : roleList) {
                if(roleId.equals(role.getId())) {
                    return role.getRoleName();
                }
            }
        }
    }
}
