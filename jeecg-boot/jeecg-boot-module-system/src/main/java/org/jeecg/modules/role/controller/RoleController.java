package org.jeecg.modules.role.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.role.entity.Role;
import org.jeecg.modules.role.service.IRoleService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.users.entity.User;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 角色管理
 * @Author: hBaby
 * @Date:   2019-09-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色管理")
@RestController
@RequestMapping("/role/role")
public class RoleController {
	@Autowired
	private IRoleService roleService;

	/**
	  * 分页列表查询
	 * @param role
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "角色管理-分页列表查询")
	@ApiOperation(value="角色管理-分页列表查询", notes="角色管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Role>> queryPageList(Role role,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Role>> result = new Result<IPage<Role>>();
		QueryWrapper<Role> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
		Page<Role> page = new Page<Role>(pageNo, pageSize);
		IPage<Role> pageList = roleService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param role
	 * @return
	 */
	@AutoLog(value = "角色管理-添加")
	@ApiOperation(value="角色管理-添加", notes="角色管理-添加")
	@PostMapping(value = "/add")
    public Result<Role> add(@RequestBody JSONObject jsonObject) {
        //public Result<User> add(@RequestBody Role role) {
        Result<Role> result = new Result<Role>();
        try {
            Role role = JSON.parseObject(jsonObject.toJSONString(), Role.class);
            Role role1 = roleService.getRoleByName(role.getRoleName());
            if(role1!=null)
                return result.error500("该角色已存在！");
            role.setCreateTime(new Date());//设置创建时间
            roleService.save(role);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }


     /**
	  *  编辑
	 * @param role
	 * @return
	 */
	@AutoLog(value = "角色管理-编辑")
	@ApiOperation(value="角色管理-编辑", notes="角色管理-编辑")
	@PutMapping(value = "/edit")
	public Result<Role> edit(@RequestBody Role role) {
		Result<Role> result = new Result<Role>();
		Role roleEntity = roleService.getById(role.getId());
		if(roleEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = roleService.updateById(role);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色管理-通过id删除")
	@ApiOperation(value="角色管理-通过id删除", notes="角色管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			roleService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}

	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "角色管理-批量删除")
	@ApiOperation(value="角色管理-批量删除", notes="角色管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Role> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Role> result = new Result<Role>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.roleService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色管理-通过id查询")
	@ApiOperation(value="角色管理-通过id查询", notes="角色管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Role> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Role> result = new Result<Role>();
		Role role = roleService.getById(id);
		if(role==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(role);
			result.setSuccess(true);
		}
		return result;
	}

	 /**
	  * 通过用户名查询
	  * @param role_name
	  * @return
	  */
	 @AutoLog(value = "用户模块-通过用户名查询")
	 @ApiOperation(value="用户模块-通过用户名查询", notes="用户模块-通过用户名查询")
	 @GetMapping(value = "/queryRoleByName")
	 public Result<Role> queryByRoleName(@RequestParam(name="role_name",required=true) String role_name) {
		 Result<Role> result = new Result<Role>();
		 Role role = roleService.getRoleByName(role_name);
		 if(role==null) {
			 result.error500("未找到对应实体");
		 }else {
			 result.setResult(role);
			 result.setSuccess(true);
		 }
		 return result;
	 }
	 @RequestMapping(value = "/queryall", method = RequestMethod.GET)
	 public Result<List<Role>> queryall() {
		 Result<List<Role>> result = new Result<>();
		 List<Role> list = roleService.list();
		 if(list==null||list.size()<=0) {
			 result.error500("未找到角色信息");
		 }else {
			 result.setResult(list);
			 result.setSuccess(true);
		 }
		 return result;
	 }

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<Role> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Role role = JSON.parseObject(deString, Role.class);
              queryWrapper = QueryGenerator.initQueryWrapper(role, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Role> pageList = roleService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "角色管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Role.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("角色管理列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<Role> listRoles = ExcelImportUtil.importExcel(file.getInputStream(), Role.class, params);
              roleService.saveBatch(listRoles);
              return Result.ok("文件导入成功！数据行数:" + listRoles.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
