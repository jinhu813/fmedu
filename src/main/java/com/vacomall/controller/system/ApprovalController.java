package com.vacomall.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Apply;
import com.vacomall.entity.SysRole;
import com.vacomall.entity.SysUser;
import com.vacomall.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;

/**
 * @program:
 * @description: 审批控制器
 * @author: wangting
 * @create: 2020-01-28 09:38
 */
@Controller
@RequestMapping("/system/approval")
public class ApprovalController extends SuperController {

    private final String prefix = "system/approval/";

    @Autowired
    private ApplyService applyService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysUserRoleService userRoleService;

    @RequestMapping("/userAddapproval/{id}")
    public String userAddapproval(@PathVariable(name = "id") String id, Model model) {
        //查询apply
        Apply apply = applyService.selectById(id);
        String appId = apply.getId();
        //用户注册逻辑中，applyId也是用户id
        SysUser user = userService.selectById(appId);
        //获取userdetail
//        UserDetail userDetail = userDetailService.selectById(appId);
        //放入model中传递到前台
        model.addAttribute("apply", apply);
        model.addAttribute("user", user);
//        model.addAttribute("userDetail",userDetail);
        //获取用户角色
        Set<String> roles = userRoleService.findRolesByUid(appId);
        if (!CollectionUtils.isEmpty(roles)) {
            HashSet<String> roleSet = (HashSet<String>) roles;
            String roleId = roleSet.iterator().next();//集合中的第一个roleId
            //查询角色名称
            SysRole sysRole = roleService.selectById(roleId);
            model.addAttribute("role",sysRole);
        }
        return prefix + "userAddapproval";
    }

    @RequestMapping("/doUseraddApproval")
    @RequiresPermissions("user:add:approve")
    @ResponseBody
    public Rest doUserAddApproval(String applyId,Long state){
        Apply apply = applyService.selectById(applyId);
        //设置app
        //用户注册逻辑中，applyId也是用户id
        SysUser user = userService.selectById(applyId);
        //未通过，用户状态-1是禁用
        if(state.equals(1)){
            user.setUserState(-1);
        }else{
            user.setUserState(1);
        }
        apply.setState(state);
        applyService.updateApply(apply);
        userService.updateById(user);
        return Rest.ok();
    }

}
