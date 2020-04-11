package com.vacomall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.SysUser;
import com.vacomall.mapper.LessonMapper;
import com.vacomall.service.LessonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName LessonServiceImpl
 * @Description: TODO
 * @Author jimbo
 * @Date 2020/1/20 
 * @Version V1.0
 **/
@Service
public class LessonServiceImpl extends ServiceImpl<LessonMapper,Lesson> implements LessonService {

    /**
     * 分页获取我的课程
     * 需要区分学生用户，还是教师用户
     * @param page
     * @param lesson
     * @return
     */
    @Override
    public Page<Lesson> mineLessons(Page<Lesson> page, Lesson lesson) {
        //判断是不是学生用户
        boolean student = SecurityUtils.getSubject().hasRole("STUDENT");
        //获取当前用户信息
        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();

        Wrapper wrapper = new EntityWrapper<Lesson>();
        //根据名称查询
        if(StringUtils.isNotEmpty(lesson.getLessonName())){
            wrapper.and().like("LESSON_NAME",lesson.getLessonName());
        }

        //根据课程状态查询
        if(null != lesson.getState()){
            wrapper.and().eq("state",lesson.getState());
        }

        if(student){
            //如果是学生用户，直接获取STU_ID 是当前用户id的课程
            wrapper.eq("STU_ID",currentUser.getId());
        }else{
            //是教师用户的话，Teach_id是当前用户的id，且需要按照info_id去重，因为一个课程会有多个学生
            //一个学生会有一条lesson记录
            wrapper.eq("TEACH_ID",currentUser.getId());
            wrapper.groupBy("INFO_ID");
        }
        return this.selectPage(page,wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lessonDone(String lessonId) {
        Lesson lesson = this.selectById(lessonId);
        //因为一个教师用户的一门课程里会有多个学生，一个学生就有一条lesson记录，
        //所以要查到所有学生的该门课程，并更新状态
        Wrapper<Lesson> wrapper = new EntityWrapper<>();
        wrapper.eq("Teach_id",lesson.getTeachId());
        wrapper.and().eq("INFO_ID",lesson.getInfoId());
        List<Lesson> lessons = this.selectList(wrapper);
        List<String> ids = new ArrayList<>();
        for(Lesson l : lessons){
            ids.add(l.getId());
        }
        baseMapper.lessonDone(ids);
    }
}
