package com.vacomall.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.vacomall.common.bean.Rest;
import com.vacomall.entity.SysRole;
import com.vacomall.entity.SysUserRole;
import com.vacomall.entity.UserDetail;
import com.vacomall.service.ISysRoleService;
import com.vacomall.service.ISysUserRoleService;
import com.vacomall.service.UserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vacomall.common.controller.SuperController;
import com.vacomall.common.util.CommonUtil;
import com.vacomall.common.util.ShiroUtil;
import com.vacomall.entity.SysUser;
import com.vacomall.service.ISysUserService;

import java.util.List;
import java.util.Map;

/**
 * 用户中心控制器
 * @author wangting
 * @date 2019年12月16日 下午4:24:04
 */
@Controller
@RequestMapping("/system/me")
public class MeController extends SuperController{  
	
	@Autowired private ISysUserService sysUserService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private ISysUserRoleService userRoleService;
	
	/**
	 * 个人信息
	 * @param model
	 * @return
	 */
    @RequestMapping("/info")  
    public  String info(Model model){
    	
    	SysUser sysUser = sysUserService.selectById(ShiroUtil.getSessionUid());
		UserDetail userDetail = userDetailService.selectById(sysUser.getId());
		Map<String, Object> moreInfo = userDetailService.getUserMoreInfo(sysUser.getId());
		model.addAttribute("moreInfo",moreInfo);
		model.addAttribute("detail",userDetail);
		model.addAttribute("sysUser", sysUser);
		return "system/me/info2";
    } 
    
    
    /**
	 * 修改密码页面
	 * @param model
	 * @return
	 */
    @RequestMapping("/pwd")  
    public  String pwd(Model model){
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		List<SysRole> sysRoles = roleService.selectList(null);
		EntityWrapper<SysUserRole> ew = new EntityWrapper<SysUserRole>();
		ew.eq("userId ", user.getId());
		List<SysUserRole> mySysUserRoles = userRoleService.selectList(ew);
		List<String> myRolds = Lists.transform(mySysUserRoles, input -> input.getRoleId());

		UserDetail userDetail = userDetailService.selectById(user.getId());
		model.addAttribute("userDetail",userDetail);
		model.addAttribute("sysUser",user);
		model.addAttribute("sysRoles",sysRoles);
		model.addAttribute("myRolds",myRolds);
		return "system/me/pwd";
    } 
    
    /**
     * 修改密码
     */
    @RequestMapping("/doChangePwd")
    public String doChangePwd(String password,String newpassword,String newpassword2,Model model,RedirectAttributes redirectAttributes){
    	
    	if(StringUtils.isBlank(password) || StringUtils.isBlank(newpassword) || StringUtils.isBlank(newpassword2)){
    		redirectAttributes.addFlashAttribute("msg","客户端提交数据不能为空.");
    		return redirectTo("/system/me/pwd");
    	}
    	
    	Subject subject = SecurityUtils.getSubject();
		SysUser sysUser = (SysUser) subject.getPrincipal();
    	
    	SysUser user = sysUserService.selectById(sysUser.getId());
    	if(!user.getPassword().equals(CommonUtil.MD5(password))){
    		redirectAttributes.addFlashAttribute("msg","旧密码输入错误.");
    		return redirectTo("/system/me/pwd");
    	}
    	
    	if(!newpassword2.equals(newpassword)){
    		redirectAttributes.addFlashAttribute("msg","两次输入的密码不一致.");
    		return redirectTo("/system/me/pwd");
    	}
    	
    	user.setPassword(CommonUtil.MD5(newpassword));
    	sysUserService.updateById(user);
    	
    	redirectAttributes.addFlashAttribute("info","密码修改成功.");
    	return redirectTo("/system/me/pwd");
    }
    
    /**
     * 更新用户
     * @param sysUser
     * @param model
     * @return
     */
    @RequestMapping("/updateUser")
	@ResponseBody
    public Rest updateUser(SysUser sysUser,String  phone, Model model, RedirectAttributes redirectAttributes){
    	
    	SysUser user = sysUserService.selectById(sysUser.getId());
    	if(StringUtils.isNotEmpty(sysUser.getPassword())){
			user.setPassword(ShiroUtil.md51024Pwd(sysUser.getPassword(), sysUser.getUserName()));
		}
    	if(StringUtils.isNotBlank(sysUser.getUserImg())){
    		user.setUserImg(sysUser.getUserImg());
    	}
    	user.setUserName(sysUser.getUserName());
    	user.setUserDesc(sysUser.getUserDesc());
    	sysUserService.updateById(user);
    	model.addAttribute("sysUser", user);

    	//更新电话
		UserDetail userDetail = userDetailService.selectById(user.getId());
		userDetail.setPhone(phone);
		userDetailService.updateById(userDetail);
		redirectAttributes.addFlashAttribute("info","更新成功.");
    	return Rest.ok("信息更新成功");
    }
}
