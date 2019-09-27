package org.jeecg.modules.role.entity;

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
 * @Description: 角色管理
 * @Author: jeecg-boot
 * @Date:   2019-09-25
 * @Version: V1.0
 */
@Data
@TableName("role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="role对象", description="角色管理")
public class Role {

	/**角色id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "角色id")
	private java.lang.String id;
	/**角色名称*/
	@Excel(name = "角色名称", width = 15)
    @ApiModelProperty(value = "角色名称")
	private java.lang.String roleName;
	/**角色值*/
	@Excel(name = "角色值", width = 15)
    @ApiModelProperty(value = "角色值")
	private java.lang.String value;
	/**角色描述*/
	@Excel(name = "角色描述", width = 15)
    @ApiModelProperty(value = "角色描述")
	private java.lang.String description;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
}
