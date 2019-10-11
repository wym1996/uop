package org.jeecg.modules.role_permission.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 角色权限表
 * @Author: hBaby
 * @Date: 2019-10-10
 * @Version: V1.0
 */
@Data
@TableName("role_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "role_permission对象", description = "角色权限表")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 角色id
     */
    @Excel(name = "角色id", width = 15)
    @ApiModelProperty(value = "角色id")
    private java.lang.String roleId;
    /**
     * 权限id
     */
    @Excel(name = "权限id", width = 15)
    @ApiModelProperty(value = "权限id")
    private java.lang.String permissionId;

    public RolePermission() {
    }

    public RolePermission(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
