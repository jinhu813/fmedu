package com.vacomall.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.Reserved;
import com.vacomall.entity.SysUser;
import com.vacomall.service.LessonService;
import com.vacomall.service.ReservedService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: fmedu
 * @description: 我的预约
 * @author:
 * @create: 2020-03-14 20:04
 */
@Controller
@RequestMapping("/system/reserved")
public class ReservedController extends SuperController {

    @Autowired
    private ReservedService reservedService;

    private String prefix = "system/reserved/";

    @RequestMapping("/list/{type}/{pageNo}")
    public String list(Model model, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,@PathVariable("type") String type) {
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        Wrapper wrapper = new EntityWrapper<Reserved>();
        if("mine".equals(type)){
            //我的预约
            wrapper.eq("CREATED_ID", userId);
            model.addAttribute("mine",true);
        }else{
            //预约我的
            wrapper.eq("for_id",userId);
            model.addAttribute("mine",false);
        }
        wrapper.orderBy("CREATED_TIME",false);
        Page<Reserved> page = this.getPage(pageNo, pageSize);
        Page result = reservedService.selectPage(page, wrapper);
        model.addAttribute("pageData", result);
        return prefix + "/list";
    }

    /**
     * 去添加页面
     * @param lessonId
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(String lessonId,Model model){
        model.addAttribute("lessonId",lessonId);
        Lesson lesson = lessonService.selectById(lessonId);
        model.addAttribute("lesson",lesson);
        return prefix +"/add";
    }

    @Autowired
    private LessonService lessonService;
    @RequestMapping("/doAdd")
    @ResponseBody
    public Rest doAdd(Reserved reserved,String daterange) throws Exception{
        //2020/03/14 21:00
        String[] dates = daterange.split("--");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date begin = new Date(simpleDateFormat.parse(dates[0]).getTime());
        Date end = new Date(simpleDateFormat.parse(dates[1]).getTime());
        reserved.setBegin(begin);
        reserved.setEnd(end);
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        reserved.setCreatedId(userId);
        //查询课程信息
        Lesson lesson = lessonService.selectById(reserved.getLessonId());
        if(lesson == null){
            return Rest.failure("预约失败，课程信息不存在");
        }
        reserved.setCreatedTime(new Date());
        reserved.setLessonName(lesson.getLessonName());
        reserved.setState(0);
        reserved.setCreatedBy(user.getUserName());
        if(isStudent()){
            //是学生，就要像老师申请
            reserved.setForId(lesson.getTeachId());
            reserved.setForName(lesson.getTeachName());
        }else{
            //是项学生申请
            reserved.setForId(lesson.getStuId());
            reserved.setForName(lesson.getStuName());
        }
        reservedService.insert(reserved);
        return Rest.ok("申请成功！");
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Rest deal(String id){
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        Reserved reserved = reservedService.selectById(id);
        if(!reserved.getState().equals(0)){
            //不等于0说明已审核
            return  Rest.failure("已审核");
        }

        Wrapper wrapper = new EntityWrapper<Reserved>();
        wrapper.eq("for_id",userId);
        wrapper.and().between("end_time", reserved.getBegin(), reserved.getEnd());
        wrapper.and().eq("state",1);
        int i = reservedService.selectCount(wrapper);
        if(i > 0){
            reserved.setState(2);
            reservedService.updateById(reserved);
            return Rest.failure("预约时间段已被预约，系统自动设置未通过");
        }else{
            reserved.setState(1);
            reservedService.updateById(reserved);
            return Rest.ok("审核通过");
        }
    }

}
