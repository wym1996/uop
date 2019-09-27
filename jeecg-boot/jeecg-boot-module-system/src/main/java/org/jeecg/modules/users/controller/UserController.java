package org.jeecg.modules.users.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.user_role.entity.UserRole;
import org.jeecg.modules.user_role.service.IUserRoleService;
import org.jeecg.modules.users.entity.User;
import org.jeecg.modules.users.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 用户模块
 * @Author: hBaby
 * @Date:   2019-09-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户模块")
@RestController
@RequestMapping("/users/user")
public class UserController {
	@Autowired
	private IUserService userService;
	 @Autowired
	 private IUserRoleService userRoleService;


	/**
	  * 分页列表查询
	 * @param user
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户模块-分页列表查询")
	@ApiOperation(value="用户模块-分页列表查询", notes="用户模块-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<User>> queryPageList(User user,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<User>> result = new Result<IPage<User>>();
		QueryWrapper<User> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
		Page<User> page = new Page<User>(pageNo, pageSize);
		IPage<User> pageList = userService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param user
	 * @return
	 */
	@AutoLog(value = "用户模块-添加")
	@ApiOperation(value="用户模块-添加", notes="用户模块-添加")
	@PostMapping(value = "/add")
	//@RequiresPermissions("user:add")
	public Result<User> add(@RequestBody JSONObject jsonObject) {
	//public Result<User> add(@RequestBody User user) {
		Result<User> result = new Result<User>();
		String selectedRoles = jsonObject.getString("selectedroles");
		try {
			User user = JSON.parseObject(jsonObject.toJSONString(), User.class);
			user.setCreateTime(new Date());//设置创建时间
			//userService.save(user);
			userService.addUserWithRole(user, selectedRoles);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param user
	 * @return
	 */
	@AutoLog(value = "用户模块-编辑")
	@ApiOperation(value="用户模块-编辑", notes="用户模块-编辑")
	@PutMapping(value = "/edit")
//	public Result<User> edit(@RequestBody User user) {
	public Result<User> edit(@RequestBody JSONObject jsonObject) {
		Result<User> result = new Result<User>();

		try {
			User user = userService.getById(jsonObject.getString("id"));
			//System.out.println(jsonObject);
			if(user==null) {
				result.error500("未找到对应实体");
			}else {
				User users = JSON.parseObject(jsonObject.toJSONString(), User.class);
				//System.out.println(users);
				users.setUpdateTime(new Date());
				//String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
				//user.setPassword(user.getPassword());
				String roles = jsonObject.getString("selectedroles");
				System.out.println(roles);
				userService.editUserWithRole(users, roles);
				result.success("修改成功!");
			}
		} catch (Exception e) {
			//	log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户模块-通过id删除")
	@ApiOperation(value="用户模块-通过id删除", notes="用户模块-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			userService.removeById(id);
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
	@AutoLog(value = "用户模块-批量删除")
	@ApiOperation(value="用户模块-批量删除", notes="用户模块-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<User> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<User> result = new Result<User>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户模块-通过id查询")
	@ApiOperation(value="用户模块-通过id查询", notes="用户模块-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<User> queryById(@RequestParam(name="id",required=true) String id) {
		Result<User> result = new Result<User>();
		User user = userService.getById(id);
		if(user==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(user);
			result.setSuccess(true);
		}
		return result;
	}

	 @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
	 public Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid) {
		 Result<List<String>> result = new Result<>();
		 List<String> list = new ArrayList<String>();
		 List<UserRole> userRole = userRoleService.list(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userid));
		 if (userRole == null || userRole.size() <= 0) {
			 result.error500("未找到用户相关角色信息");
		 } else {
			 for (UserRole userRoles : userRole) {
				 list.add(userRoles.getRoleId());
			 }
			 result.setSuccess(true);
			 result.setResult(list);
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
      QueryWrapper<User> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              User user = JSON.parseObject(deString, User.class);
              queryWrapper = QueryGenerator.initQueryWrapper(user, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<User> pageList = userService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户模块列表");
      mv.addObject(NormalExcelConstants.CLASS, User.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户模块列表数据", "导出人:Jeecg", "导出信息"));
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
              List<User> listUsers = ExcelImportUtil.importExcel(file.getInputStream(), User.class, params);
              userService.saveBatch(listUsers);
              return Result.ok("文件导入成功！数据行数:" + listUsers.size());
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
