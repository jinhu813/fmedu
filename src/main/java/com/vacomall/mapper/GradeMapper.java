package com.vacomall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.vacomall.entity.Grade;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.vo.LessonVo;

import java.util.List;

/**
 * @program: AdminLTE-admin
 * @description: 用户评分
 * @author: wangting
 * @create: 2020-02-15 09:37
 */
public interface GradeMapper extends BaseMapper<Grade> {

    List<LessonVo> MarkLesson(Lesson lesson);
}
