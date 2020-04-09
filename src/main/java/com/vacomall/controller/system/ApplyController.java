package com.vacomall.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.*;
import com.vacomall.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program:
 * @description: 申请 学员申请课程，教师申请学员课程Controller
 * @author: wnagting
 * @create: 2020-01-21 08:58
 */
@Controller
@RequestMapping("/system/apply")
public class ApplyController extends SuperController {

    private String prefix = "system/apply/";

    @Autowired
    private ApplyService applyService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private DictService dictService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private UserDetailService userDetailService;


    /**
     * 查看申请列表申请分为我申请的和申请我的(我的审批)
     * 这个方法是获取我申请的
     *
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String apply(Model model, Apply apply, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        //构建查询条件，取我申请的，所以applyUserId为当前用户id
        apply.setApplyUserId(userId);
        //构建分页对象
        Page<Apply> page = getPage(pageNo, pageSize);
        Page<Apply> applyPage = applyService.selectApplyByPage(apply, page);
        model.addAttribute("infoTitle", apply.getInfoTitle());
        model.addAttribute("state", apply.getState());
        model.addAttribute("pageData", applyPage);
        return prefix + "/list";
    }

    /**
     * 我的审批
     *
     * @param model
     * @param apply
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/approve/{pageNo}")
    public String approved(Model model, Apply apply, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        //构建查询条件
        apply.setPublishId(userId);
        //构建分页对象
        Page<Apply> page = getPage(pageNo, pageSize);
        Page<Apply> applyPage = applyService.selectApplyByPage(apply, page);
        model.addAttribute("pageData", applyPage);
        model.addAttribute("infoTitle", apply.getInfoTitle());
        model.addAttribute("state", apply.getState());
        return prefix + "/approveList";
    }

    @RequestMapping("/userAdd/{pageNo}")
    public String userAddApply(Model model, Apply apply, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //构建查询条件,PublishId=user_add的是用户注册审批
        apply.setPublishId("user_add");
        //构建分页对象
        Page<Apply> page = getPage(pageNo, pageSize);
        Page<Apply> applyPage = applyService.selectApplyByPage(apply, page);
        model.addAttribute("pageData", applyPage);
        return prefix + "/userAddList";
    }

    /**
     * 申请页面
     *
     * @return
     */
    @RequestMapping("/{infoId}")
    @ResponseBody
    public Rest apply(@PathVariable(name = "infoId") String infoId, Model model) {
        Rest rest = Rest.failure("保存成功");
        Information information = informationService.selectInformationById(infoId);
        //获取当前申请的用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        boolean saved = applyService.save(information, user);
        if (!saved) {
            rest = Rest.failure("保存失败");
        }
        return rest;
    }

    /**
     * 详情查看
     *
     * @param id
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable(name = "id") String id, Model model) {
        //查询apply
        Apply apply = applyService.selectById(id);
        model.addAttribute("apply", apply);
        //查询infomation
        Information information = informationService.selectById(apply.getInfoId());
        model.addAttribute("info", information);
        if(null != information) {
            UserDetail userDetail = userDetailService.selectById(information.getCreatedUserId());
            model.addAttribute("userDetail", userDetail);
        }

        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("lessons", dicts);
        return prefix + "view";
    }

    @RequestMapping("/cancel/{id}")
    @ResponseBody
    public Rest cancel(@PathVariable String id) {
        try {
            Apply apply = applyService.selectById(id);
            apply.setState(3L);
            applyService.updateApply(apply);
        } catch (Exception e) {
            e.printStackTrace();
            return Rest.failure("取消失败");
        }
        return Rest.ok("成功取消");
    }

    /**
     * 去我的审批的审批页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/todoApproval/{id}")
    public String todoApproval(@PathVariable(name = "id") String id, Model model) {
        Apply apply = applyService.selectById(id);
        //获取申请人信息
        String applyUserId = apply.getApplyUserId();
        SysUser user = userService.selectById(applyUserId);
        //查询发布的信息
        Information information = informationService.selectById(apply.getInfoId());
        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("lessons", dicts);
        model.addAttribute("info", information);
        model.addAttribute("applyUser", user);
        model.addAttribute("apply", apply);

        return prefix+"todoApproval";
    }

    @RequestMapping("/doApproval")
    @ResponseBody
    public Rest doApproval(Apply apply){
        try {
            this.applyService.doApproval(apply);
        }catch (Exception e){
            return Rest.failure("审核失败");
        }
        return Rest.ok();
    }
}
