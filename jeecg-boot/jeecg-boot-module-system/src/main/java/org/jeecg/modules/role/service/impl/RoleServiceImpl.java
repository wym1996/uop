package org.jeecg.modules.role.service.impl;

import org.jeecg.modules.role.entity.Role;
import org.jeecg.modules.role.mapper.RoleMapper;
import org.jeecg.modules.role.service.IRoleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 角色管理
 * @Author: jeecg-boot
 * @Date:   2019-09-25
 * @Version: V1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
