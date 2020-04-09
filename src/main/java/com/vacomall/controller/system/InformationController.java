package com.vacomall.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Dict;
import com.vacomall.entity.Information;
import com.vacomall.entity.SysRole;
import com.vacomall.entity.SysUser;
import com.vacomall.service.DictService;
import com.vacomall.service.ISysRoleService;
import com.vacomall.service.InformationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @program:
 * @description: 发布的信息 招聘信息、求职信息Controller
 * @author: wnagting
 * @create: 2020-01-20 18:36
 */
@Controller
@RequestMapping("/system/information")
public class InformationController extends SuperController {

    private String prefix = "system/information/";

    @Autowired
    private InformationService informationService;

    @Autowired
    private DictService dictService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 分页获取当前用户发布的信息
     *
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(Model model, @PathVariable(name = "pageNo") Integer pageNo, Information information,
                       @RequestParam(defaultValue = "15") Integer pageSize, Long state) {

        Page<Information> page = this.getPage(pageNo, pageSize);
        //获取当前登陆用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //判断当前用户是不是管理员,管理员则查询条件不添加用户id查询
        boolean admin = isAdmin();
        if (!admin) {
            //设置当前用户的id，表示只查询当前用户的发布信息
            information.setCreatedUserId(user.getId());
        }
        //设置状态
        if (null != state) {
            information.setState(state);
        }

        Page<Information> informationPage = informationService.selectInfomationByPage(page, information);
        model.addAttribute("pageData", informationPage);
        //用于页面查询条件的回显
        model.addAttribute("state", information.getState());
        model.addAttribute("title", information.getTitle());

        return prefix + "list";
    }

    /**
     * 查询未审批的信息
     *
     * @param model
     * @param pageNo
     * @param information
     * @param pageSize
     * @return
     */
    @RequestMapping("/front/list/{pageNo}")
    public String adminList(Model model, @PathVariable(name = "pageNo") Integer pageNo, Information information,
                            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Information> page = this.getPage(pageNo, pageSize);

        information.setState(0L);
        Page<Information> informationPage = informationService.selectInfomationByPage(page, information);
        model.addAttribute("content", informationPage);

        return prefix + "frontlist";
    }

    @RequiresPermissions("system:info:add")
    @RequestMapping("/toAdd")
    public String toAdd(Model model) {
        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("lessons", dicts);
        Information information = new Information();
        model.addAttribute("info", information);
        return prefix + "add";
    }


    @RequiresPermissions("system:info:add")
    @RequestMapping("/doAdd")
    @ResponseBody
    public Rest doAdd(Information information) {
        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
//        Wrapper wrapper = new EntityWrapper<SysRole>();
//        wrapper.eq("roleName", "STUDENT");
//        SysRole sysRole = roleService.selectOne(wrapper);
        boolean student = SecurityUtils.getSubject().hasRole("STUDENT");
        if (student) {
            information.setType(1L);
        } else {
            information.setType(2L);
        }

        information.setCreatedTime(new Date());
        information.setState(0L);
        information.setCreatedUserId(currentUser.getId());
        information.setCreatedUserName(currentUser.getUserName());
        if (StringUtils.isEmpty(information.getLessonTypeId())) {
            return Rest.failure("课程信息有误");
        }
        //获取课程名称
        List<Dict> dicts = dictService.selectList("LESSON", information.getLessonTypeId());
        if (!CollectionUtils.isEmpty(dicts)) {
            String name = dicts.get(0).getName();
            information.setLessonTypeName(name);
        } else {
            return Rest.failure("课程信息有误");
        }
        this.informationService.insertInformation(information);
        return Rest.ok();
    }


    @RequestMapping("/view/{id}")
    public String view(@PathVariable(name = "id") String id, Model model) {
        Information information = this.informationService.selectInformationById(id);
        model.addAttribute("info", information);

        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("lessons", dicts);
        return prefix + "view";
    }

    @RequestMapping("/approval/{id}")
    @RequiresPermissions("system:info:approval")
    public String approval(@PathVariable(name = "id") String id, Model model) {
        Information information = this.informationService.selectInformationById(id);
        model.addAttribute("info", information);

        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("lessons", dicts);
        return prefix + "approval";
    }

    /**
     * 审批
     *
     * @param
     * @return
     */
    @RequestMapping("/doApproval")
    @RequiresPermissions("system:info:approval")
    @ResponseBody
    public Rest doApproval(Information information) {
        //获取infomation中的id，审批意见，和审批结果
        String id = information.getId();
        String denyReson = information.getDenyReson();
        Long state = information.getState();
        //根据id查询，并设置查询结果的审批意见和审批结果
        Information info = informationService.selectInformationById(id);
        info.setState(state);
        info.setDenyReson(denyReson);
        info.setUpdatedTime(new Date());

        //更新
        boolean b = informationService.updateInformation(info);
        if (!b) {
            //更新失败
            return Rest.failure("更新失败");
        }

        return Rest.ok();
    }

    @RequiresPermissions("system:info:delete")
    @RequestMapping("/{id}")
    @ResponseBody
    public Rest delete(@PathVariable String id) {
        try {
            boolean sucess = this.informationService.deleteInformationById(id);
            if (!sucess) {
                return Rest.failure("删除失败");
            }
        } catch (Exception e) {
            return Rest.failure("系统发现异常");
        }
        return Rest.ok();
    }

    @RequiresPermissions("system:info:disabled")
    @RequestMapping("/disabled/{id}")
    @ResponseBody
    public Rest disabled(@PathVariable String id){
        //先查询
        Information information = informationService.selectById(id);
        information.setState(4L);
        //更新
        informationService.updateById(information);
        return Rest.ok();
    }
}
