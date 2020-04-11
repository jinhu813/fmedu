package com.vacomall.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Apply;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.SysUser;
import com.vacomall.service.DictService;
import com.vacomall.service.LessonService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName LessonController
 * @Description: TODO
 * @Author jimbo
 * @Date 2020/1/20 
 * @Version V1.0
 **/
@Controller
@RequestMapping("/system/lesson")
public class LessonController extends SuperController {

    private String prefix = "system/lesson";
    @Autowired
    private LessonService lessonService;
    @Autowired
    private DictService dictService;

    @RequiresPermissions("system:LESSON:view")
    @GetMapping()
    public String LESSON()
    {
        return prefix + "/LESSON";
    }


    /**
     * 新增课程
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 修改课程
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        return prefix + "/edit";
    }

    @RequestMapping("/list/{pageNo}")
    public String apply(Model model, Lesson lesson, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Lesson> page = getPage(pageNo, pageSize);
        Page<Lesson> mineLessons = lessonService.mineLessons(page, lesson);
        model.addAttribute("pageData",mineLessons);
        //用于前端页面查询条件的回显
        model.addAttribute("lessonName",lesson.getLessonName());
        model.addAttribute("state",lesson.getState());
        boolean student = isStudent();
        model.addAttribute("student",student);
        return prefix + "/list";
    }

    @RequestMapping("/done/{lessonId}")
    @ResponseBody
    public Rest done(@PathVariable(name = "lessonId") String lessonId){
        this.lessonService.lessonDone(lessonId);
        return Rest.ok();
    }
}
