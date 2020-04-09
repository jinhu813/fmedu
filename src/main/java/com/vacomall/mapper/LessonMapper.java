package com.vacomall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.vacomall.entity.Lesson;

import java.util.List;

/**
 * @ClassName LessonMapper
 * @Description: TODO
 * @Author jimbo
 * @Date 2020/1/20 
 * @Version V1.0
 **/
public interface LessonMapper  extends BaseMapper<Lesson> {

    void lessonDone(List<String> ids);
}
