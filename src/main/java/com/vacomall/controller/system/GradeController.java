package com.vacomall.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Dict;
import com.vacomall.entity.Grade;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.vo.LessonVo;
import com.vacomall.service.DictService;
import com.vacomall.service.GradeService;
import com.vacomall.service.LessonService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @program: AdminLTE-admin
 * @description: 用户评分控制层
 * @author: wangting
 * @create: 2020-02-15 09:40
 */
@Controller
@RequestMapping("/system/grade")
public class GradeController extends SuperController {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private DictService dictService;

    private String prefix = "system/grade/";
    /**
     * 学生获取我的课程评分
     * @return
     */
    @RequestMapping("/stumark/{pageNo}")
    public String MarkLesson(Model model, Lesson lesson, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        //获取已完成的课程
        lesson.setState(2L);
        Page<LessonVo> page = getPage(pageNo, pageSize);
        Page<LessonVo> lessons = gradeService.stuMarkLesson(page, lesson);
        model.addAttribute("isTeacher",isTeacher());
        model.addAttribute("pageData",lessons);
        //用于前端页面查询条件的回显
        model.addAttribute("lessonName",lesson.getLessonName());
        return prefix+"stumark";
    }

    /**
     * 去评分页面
     * @param lessonId
     * @return
     */
    @RequestMapping("/toMark/{lessonId}")
    public String stuToMark(@PathVariable(name = "lessonId") String lessonId,Model model){
        //判断当前用户是不是学生角色
        boolean isStudent = SecurityUtils.getSubject().hasRole("STUDENT");
        //前端页面会根据这个值显示不同的字段
        model.addAttribute("isStudent",isStudent);
        //查询课程信息
        Lesson lesson = lessonService.selectById(lessonId);
        //从字典项中获取课程类型
        List<Dict> dicts = dictService.findByTypeCode("LESSON");
        model.addAttribute("dicts", dicts);
        model.addAttribute("lesson",lesson);
        return prefix+"doMark";
    }

    @RequestMapping("/domark")
    @ResponseBody
    public Rest domark(Grade grade){
        try {
            this.gradeService.doMark(grade);
        }catch (Exception e){
        }
        return Rest.ok("完成评分");
    }

    /**
     * 获取个人评分信息
     * @return
     */
    @RequestMapping("/mineGrade/{pageNo}")
    public String mineGrade(Model model, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        Page<Grade> page = getPage(pageNo, pageSize);
        Page<Grade> mineGrade = this.gradeService.mineGrade(page);
        boolean teacher = isTeacher();
        model.addAttribute("teacher",teacher);
        model.addAttribute("pageData",mineGrade);
        return prefix+"mine";
    }
}
