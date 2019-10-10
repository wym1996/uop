package org.jeecg.modules.role_permission.service;

import org.jeecg.modules.role_permission.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 角色权限表
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
public interface IRolePermissionService extends IService<RolePermission> {

    /**
     * 保存授权/先删后增
     * @param roleId
     * @param permissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds,String lastPermissionIds);
}
