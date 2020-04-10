package com.vacomall.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Dict;
import com.vacomall.entity.Information;
import com.vacomall.entity.SysUser;
import com.vacomall.entity.UserDetail;
import com.vacomall.service.DictService;
import com.vacomall.service.ISysUserService;
import com.vacomall.service.InformationService;
import com.vacomall.service.UserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * 首页控制器
* @ClassName: IndexController
* @date 2020年2月8日 下午8:42:40
*
 */
@Controller
@RequestMapping("/")
public class IndexController extends SuperController {
	@Autowired
    private InformationService informationService;
	@Autowired
	private ISysUserService userService;
	@Autowired
    private DictService dictService;
	@Autowired
	private UserDetailService userDetailService;
    @RequestMapping({"index/{pageNo}","index"})
    public  String index(Model model,@PathVariable(name = "pageNo",required = false) Integer pageNo,
                         @RequestParam(defaultValue = "10") Integer pageSize,String province,String city,String area,String userName){
        //系统登陆后的首页
        getInfos(pageNo, pageSize, model,province,city,area,userName);
        //判断用户是否已经登陆
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        model.addAttribute("authenticated",authenticated);
        //下面代码用来判断是不是显示申请按钮，若是教师则无法申请另一个教师的课程
        //若是学生则无法申请另一个学生的招聘
        if(isStudent()){
            model.addAttribute("type",1);
        }else if(isTeacher()){
            model.addAttribute("type",2);
        }else {
            model.addAttribute("type",3);
        }
        return "index";
    }

    @RequestMapping({"/{pageNo}","/","welcome/{pageNo}"})
    public String welcome(@PathVariable(name = "pageNo",required = false) Integer pageNo,
                          @RequestParam(defaultValue = "50") Integer pageSize,Model model){
        //系统首页
        if(null == pageNo){
            pageNo = 1;
        }
        Page<Information> page = getPage(pageNo, pageSize);
        //查询状态为可申请的infomation
        Information info = new Information();
        //查询状态为可申请的infomation
        info.setState(1L);
        Page<Information> informationPage = informationService.selectInfomationByPage(page, info);
        model.addAttribute("pageData", informationPage);
        //判断用户是否已经登陆
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        model.addAttribute("authenticated",authenticated);
        return "welcome";
    }

    private void getInfos(@PathVariable(name = "pageNo", required = false) Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, Model model) {
        if(null == pageNo){
            pageNo = 1;
        }
        Page<Information> page = this.getPage(pageNo, pageSize);
        Information information = new Information();
        information.setState(1L);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(null != user){
            information.setCreatedUserId(user.getId());
        }

        Page<Information> informationPage = informationService.getInfoApplyPage(page, information);
        model.addAttribute("pageData", informationPage);
    }
    private void getInfos(Integer pageNo, Integer pageSize, Model model,String province,String city,String area,String userName) {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(null == pageNo){
            pageNo = 1;
        }
        Page<Information> page = this.getPage(pageNo, pageSize);
        Information information = new Information();
        information.setState(1L);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(null != user){
            information.setCreatedUserId(user.getId());
        }
        province = Optional.ofNullable(province).orElse(sysUser.getProvince());
        if(StringUtils.isNotEmpty(province)){
            model.addAttribute("province",province);
            List<Dict> cities = dictService.findByTypeCode(province);
            model.addAttribute("cities",cities);
        }
        information.setProvince(province);
        city = Optional.ofNullable(city).orElse(sysUser.getCity());
        if(StringUtils.isNotEmpty(city)){
            model.addAttribute("city",city);
            List<Dict> areas = dictService.findByTypeCode(city);
            model.addAttribute("areas",areas);
        }
        information.setCity(city);
        area = Optional.ofNullable(area).orElse(sysUser.getArea());
        if(StringUtils.isNotEmpty(area)){
            model.addAttribute("area",area);
        }

        if(StringUtils.isNotEmpty(userName)){
            model.addAttribute("userName",userName);
            information.setCreatedUserName(userName);
        }
        information.setArea(area);
        List<Dict> provinces = dictService.findByTypeCode("provinces");
        model.addAttribute("provinces",provinces);
        Page<Information> informationPage = informationService.getInfoApplyPage(page, information);
        model.addAttribute("pageData", informationPage);
    }

    /**
     * 查看信息详情
     *
     */
    @RequestMapping("/welcome/infoDetail/{id}")
    public String infoDetail(@PathVariable(name = "id") String id,Model model){
        Information information = informationService.selectInformationById(id);
        //获取发布人信息，个人评分等
        String createdUserId = information.getCreatedUserId();
        UserDetail userDetail = userDetailService.selectById(createdUserId);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("info",information);
        return "info_detail";
    }
}
