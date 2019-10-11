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
     * @Author wym
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public User getUserByName(@Param("username") String username);


    /**
     * @Author wym
     * 通过用户名在数据库中查找用户密码
     * @param username
     * @return
     */
    // public String getPasswordByUsername(String username);

}
