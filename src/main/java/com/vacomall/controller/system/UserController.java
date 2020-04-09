package com.vacomall.controller.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.vacomall.entity.*;
import com.vacomall.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.vacomall.common.anno.Log;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;

import javax.jws.WebParam;

/**
 * 用户控制器
 * @author wangting
 * @date 2020年12月30日 上午10:22:41
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends SuperController{  

	@Autowired private ISysUserService sysUserService;
	@Autowired private ISysRoleService sysRoleService;
	@Autowired private ISysUserRoleService sysUserRoleService;
	@Autowired private ISysDeptService sysDeptService;
	@Autowired private ApplyService applyService;
	
	/**
	 * 分页查询用户
	 */
	@RequiresPermissions("listUser")
    @RequestMapping("/list/{pageNumber}")  
    public  String list(@PathVariable Integer pageNumber,@RequestParam(defaultValue="15") Integer pageSize,String search,Model model){
		if(StringUtils.isNotBlank(search)){
			model.addAttribute("search", search);
		}
    	Page<Map<Object, Object>> page = getPage(pageNumber,pageSize);
    	model.addAttribute("pageSize", pageSize);
    	Page<Map<Object, Object>> pageData = sysUserService.selectUserPage(page, search);
    	model.addAttribute("pageData", pageData);
    	return "system/user/list";
    } 
    /**
     * 新增用户
     */
	@RequiresPermissions("addUser")
    @RequestMapping("/add")  
    public  String add(Model model){
    	model.addAttribute("roleList", sysRoleService.selectList(null));
    	model.addAttribute("deptList", sysDeptService.selectList(null));
		return "system/user/add";
    } 
    
    /**
     * 执行新增
     */
    @Log("创建用户")
    @RequiresPermissions("addUser")
    @RequestMapping("/doAdd")  
    @ResponseBody
	public  Rest doAdd(SysUser user,@RequestParam(value="roleId",required=false) String roleId){
		String[] roleIds = {roleId};
		sysUserService.insertUser(user,roleIds);
		return Rest.ok();
	}
	/**
     * 删除用户
     */
    @Log("删除用户")
    @RequiresPermissions("deleteUser")
    @RequestMapping("/delete")  
    @ResponseBody
    public  Rest delete(String id){
    	sysUserService.delete(id);
    	return Rest.ok();
    }  
    
	/**
	 * 编辑用户
	 */
    @RequestMapping("/edit/{id}")  
    @RequiresPermissions("editUser")
    public  String edit(@PathVariable String id,Model model){
    	SysUser sysUser = sysUserService.selectById(id);
    	
    	List<SysRole> sysRoles = sysRoleService.selectList(null);
    	EntityWrapper<SysUserRole> ew = new EntityWrapper<SysUserRole>();
    	ew.eq("userId ", id);
    	List<SysUserRole> mySysUserRoles = sysUserRoleService.selectList(ew);
    	List<String> myRolds = Lists.transform(mySysUserRoles, input -> input.getRoleId());
    	
    	model.addAttribute("sysUser",sysUser);
    	model.addAttribute("sysRoles",sysRoles);
    	model.addAttribute("myRolds",myRolds);
    	model.addAttribute("deptList", sysDeptService.selectList(null));
    	return "system/user/edit";
    } 
    /**
     * 执行编辑
     */
    @Log("编辑用户")
    @RequestMapping("/doEdit")
    @ResponseBody
	public  Rest doEdit(SysUser sysUser,@RequestParam(value="roleId",required=false) String roleId,Model model){
		String[] roleIds = {roleId};
		sysUserService.updateUser(sysUser,roleIds);
		return Rest.ok();
	}

	/**
     * 验证用户名是否已存在
     */
    @RequestMapping("/checkName")  
    @ResponseBody
    public Rest checkName(String userName){
    	List<SysUser> list = sysUserService.selectList(new EntityWrapper<SysUser>().eq("userName", userName));
    	if(list.size() > 0){
    		return Rest.failure("用户名已存在");
    	}
    	return Rest.ok();
    }

	/***
	 * 跳转到注册页面
	 * @return
	 */
	@RequestMapping("/register")
    public String register(Model model){
		//获取教师和学生角色供选择
		Wrapper<SysRole> wrapper = new EntityWrapper<>();
		wrapper.eq("roleName","STUDENT");
		wrapper.or().eq("roleName","TEACHER");
		List<SysRole> sysRoles = sysRoleService.selectList(wrapper);
		model.addAttribute("roleList",sysRoles);
		return "system/user/register";
	}

	@Autowired
	private UserDetailService userDetailService;
	/**
	 * 注册
	 * @return
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public Rest doRegister(SysUser user, UserDetail userDetail,@RequestParam(name = "roleId",required = false) String[] roleId){
		String userId = UUID.randomUUID().toString();
		user.setId(userId);
		user.setUserState(0);
		sysUserService.insertUser(user,roleId);

		userDetail.setId(userId);
		userDetailService.insert(userDetail);

		//创建一条申请
		Apply apply = new Apply();
		apply.setInfoTitle(user.getUserName()+"注册申请");
		apply.setId(userId);
		apply.setApplyTime(new Date());
		apply.setApplyUserId(userId);
		apply.setApplyUserName(user.getUserName());
		apply.setState(0L);
		apply.setInfoId(userId);
		apply.setPublishId("user_add");
		applyService.insertApply(apply);

		return Rest.ok();
	}

	@RequestMapping("/info/{id}")
	public String info(@PathVariable String id, Model model){
		SysUser sysUser = sysUserService.selectById(id);
		UserDetail userDetail = userDetailService.selectById(id);
		Map<String, Object> moreInfo = userDetailService.getUserMoreInfo(id);
		model.addAttribute("moreInfo",moreInfo);
		model.addAttribute("detail",userDetail);
		model.addAttribute("sysUser", sysUser);
		return "system/user/info";
	}
}
