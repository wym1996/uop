package org.jeecg.modules.demo.doc.entity;

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
 * @Description: FACE信息库管理
 * @Author: jeecg-boot
 * @Date:   2019-09-24
 * @Version: V1.0
 */
@Data
@TableName("doc")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="doc对象", description="FACE信息库管理")
public class doc {
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键ID")
	private String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private String docName;
	/**标准书号*/
	@Excel(name = "标准书号", width = 15)
    @ApiModelProperty(value = "标准书号")
	private String isbn;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
	private String type;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
	private String version;
	/**引用*/
	@Excel(name = "引用", width = 15)
    @ApiModelProperty(value = "引用")
	private String reference;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String description;
	/**下载路径*/
	@Excel(name = "下载路径", width = 15)
    @ApiModelProperty(value = "下载路径")
	private String downloadpath;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private Integer delFlag;
}
