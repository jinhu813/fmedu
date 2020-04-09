package com.vacomall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.Grade;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.vo.LessonVo;

/**
 * @program: AdminLTE-admin
 * @description: 用户评分
 * @author: wangting
 * @create: 2020-02-15 09:38
 */
public interface GradeService extends IService<Grade> {

    /**
     * 学生用户的我的评分列表
     * @param page
     * @param lesson
     * @return
     */
    Page<LessonVo> stuMarkLesson(Page<LessonVo> page,Lesson lesson);

    /**
     * 用户评分
     * @param grade
     */
    void doMark(Grade grade);

    Page<Grade> mineGrade(Page<Grade> page);
}
