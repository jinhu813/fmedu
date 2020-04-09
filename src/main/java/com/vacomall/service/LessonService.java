package com.vacomall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.Lesson;

import java.util.List;

/**
 * @ClassName LessonService
 * @Description: 课程
 * @Author wangting
 * @Date 2020/1/20 
 * @Version V1.0
 **/
public interface LessonService extends IService<Lesson> {

    /**
     * 分页获取我的课程
     * @param page
     * @param lesson
     * @return
     */
    Page<Lesson> mineLessons(Page<Lesson> page,Lesson lesson);

    /**
     * 课程结束
     * @param lessonId
     */
    void lessonDone(String lessonId);
}
