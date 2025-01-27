package org.jeecg.modules.users.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.users.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    public User getUserByName(@Param("username") String username);

    /**
     * 通过fid查询用户信息
     * @param fid
     * @return
     */
    public List<User> getUserByFid(@Param("fid") String fid);
}
