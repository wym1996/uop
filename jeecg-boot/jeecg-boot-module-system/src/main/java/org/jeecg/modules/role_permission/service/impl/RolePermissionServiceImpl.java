package org.jeecg.modules.role_permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.role_permission.entity.RolePermission;
import org.jeecg.modules.role_permission.mapper.RolePermissionMapper;
import org.jeecg.modules.role_permission.service.IRolePermissionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 角色权限表
 * @Author: jeecg-boot
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {
    @Override
    @CacheEvict(value = CacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    public void saveRolePermission(String roleId, String permissionIds) {
        LambdaQueryWrapper<RolePermission> query = new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId);
        this.remove(query);
        List<RolePermission> list = new ArrayList<RolePermission>();
        String[] arr = permissionIds.split(",");
        for (String p : arr) {
            if (oConvertUtils.isNotEmpty(p)) {
                RolePermission rolepms = new RolePermission(roleId, p);
                list.add(rolepms);
            }
        }
        this.saveBatch(list);
    }

    @Override
    @CacheEvict(value = CacheConstant.LOGIN_USER_RULES_CACHE, allEntries = true)
    public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds, permissionIds);
        if (add != null && add.size() > 0) {
            List<RolePermission> list = new ArrayList<RolePermission>();
            for (String p : add) {
                if (oConvertUtils.isNotEmpty(p)) {
                    RolePermission rolepms = new RolePermission(roleId, p);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds, lastPermissionIds);
        if (delete != null && delete.size() > 0) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId).eq(RolePermission::getPermissionId, permissionId));
            }
        }
    }
    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main,String diff){
        if(oConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }

}
