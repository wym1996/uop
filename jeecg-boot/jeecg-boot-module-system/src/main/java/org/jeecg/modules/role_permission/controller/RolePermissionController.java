package org.jeecg.modules.role_permission.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.role_permission.entity.RolePermission;
import org.jeecg.modules.role_permission.service.IRolePermissionService;
import java.util.Date;
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
 * @Description: 角色权限表
 * @Author: hBaby
 * @Date:   2019-10-10
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色权限表")
@RestController
@RequestMapping("/role_permission/rolePermission")
public class RolePermissionController {
	@Autowired
	private IRolePermissionService rolePermissionService;

	/**
	  * 分页列表查询
	 * @param rolePermission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "角色权限表-分页列表查询")
	@ApiOperation(value="角色权限表-分页列表查询", notes="角色权限表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<RolePermission>> queryPageList(RolePermission rolePermission,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<RolePermission>> result = new Result<IPage<RolePermission>>();
		QueryWrapper<RolePermission> queryWrapper = QueryGenerator.initQueryWrapper(rolePermission, req.getParameterMap());
		Page<RolePermission> page = new Page<RolePermission>(pageNo, pageSize);
		IPage<RolePermission> pageList = rolePermissionService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param rolePermission
	 * @return
	 */
	@AutoLog(value = "角色权限表-添加")
	@ApiOperation(value="角色权限表-添加", notes="角色权限表-添加")
	@PostMapping(value = "/add")
	public Result<RolePermission> add(@RequestBody RolePermission rolePermission) {
		Result<RolePermission> result = new Result<RolePermission>();
		try {
			rolePermissionService.save(rolePermission);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param rolePermission
	 * @return
	 */
	@AutoLog(value = "角色权限表-编辑")
	@ApiOperation(value="角色权限表-编辑", notes="角色权限表-编辑")
	@PutMapping(value = "/edit")
	public Result<RolePermission> edit(@RequestBody RolePermission rolePermission) {
		Result<RolePermission> result = new Result<RolePermission>();
		RolePermission rolePermissionEntity = rolePermissionService.getById(rolePermission.getId());
		if(rolePermissionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = rolePermissionService.updateById(rolePermission);
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
	@AutoLog(value = "角色权限表-通过id删除")
	@ApiOperation(value="角色权限表-通过id删除", notes="角色权限表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			rolePermissionService.removeById(id);
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
	@AutoLog(value = "角色权限表-批量删除")
	@ApiOperation(value="角色权限表-批量删除", notes="角色权限表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<RolePermission> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<RolePermission> result = new Result<RolePermission>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.rolePermissionService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色权限表-通过id查询")
	@ApiOperation(value="角色权限表-通过id查询", notes="角色权限表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<RolePermission> queryById(@RequestParam(name="id",required=true) String id) {
		Result<RolePermission> result = new Result<RolePermission>();
		RolePermission rolePermission = rolePermissionService.getById(id);
		if(rolePermission==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(rolePermission);
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
      QueryWrapper<RolePermission> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              RolePermission rolePermission = JSON.parseObject(deString, RolePermission.class);
              queryWrapper = QueryGenerator.initQueryWrapper(rolePermission, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<RolePermission> pageList = rolePermissionService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "角色权限表列表");
      mv.addObject(NormalExcelConstants.CLASS, RolePermission.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("角色权限表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<RolePermission> listRolePermissions = ExcelImportUtil.importExcel(file.getInputStream(), RolePermission.class, params);
              rolePermissionService.saveBatch(listRolePermissions);
              return Result.ok("文件导入成功！数据行数:" + listRolePermissions.size());
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
