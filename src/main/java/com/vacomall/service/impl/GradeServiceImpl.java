package com.vacomall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Grade;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.SysUser;
import com.vacomall.entity.UserDetail;
import com.vacomall.entity.vo.LessonVo;
import com.vacomall.mapper.GradeMapper;
import com.vacomall.service.GradeService;
import com.vacomall.service.LessonService;
import com.vacomall.service.UserDetailService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @program: AdminLTE-admin
 * @description: 用户评分
 * @author: liruihui
 * @create: 2020-02-15 09:39
 */
@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserDetailService userDetailService;

    @Override
    public Page<LessonVo> stuMarkLesson(Page<LessonVo> page, Lesson lesson) {
        //获取当前用户id
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //判断是教师还是学生
        boolean student = SecurityUtils.getSubject().hasRole("STUDENT");
        String userId = user.getId();
        if(student){
            //是学生的话设置当前用户id为stuId
            lesson.setStuId(userId);
        }else{
            lesson.setTeachId(userId);
        }
        page.setRecords(this.baseMapper.MarkLesson(lesson));
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doMark(Grade grade) {
        grade.setCreateTime(new Date());
        //获取当前用户信息
        SysUser user  = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //设置评价是当前用户发起的
        grade.setFromUid(user.getId());
        grade.setFromUname(user.getUserName());
        //判断当前用户角色是不是学生
        boolean isStudent = SecurityUtils.getSubject().hasRole("STUDENT");
        //根据lessonId获取课程信息
        String lessonId = grade.getLessonId();
        Lesson lesson = lessonService.selectById(lessonId);
        //如果课程为空无法评价
        if(null == lesson){
            return;
        }else{
            grade.setLessonName(lesson.getLessonName());
            //如果是学生，则设置Grade的toUid为lesson的teachId，否则设置设置Grade的toUid为lesson的stuId
            if(isStudent){
                grade.setToUid(lesson.getTeachId());
                grade.setType(1L);
            }else{
                grade.setToUid(lesson.getStuId());
                grade.setType(2L);
            }
            //保存评分信息
            this.insert(grade);
        }
        //评分结束后要重新计算被评论者的分数
        Long countScore = this.countScore(grade.getToUid());
        //获取用户详情并更新
        UserDetail userDetail = userDetailService.selectById(grade.getToUid());
        userDetail.setScore(countScore);
        userDetailService.updateById(userDetail);
    }

    @Override
    public Page<Grade> mineGrade(Page<Grade> page) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        Wrapper<Grade> wrapper = new EntityWrapper<>();
        wrapper.eq("to_uid",user.getId());
        Page<Grade> gradePage = this.selectPage(page, wrapper);
        return gradePage;
    }

    /**
     * 根据用户id计算平均得分
     * @param userId
     * @return
     */
    public Long countScore(String userId){
        //先从评分中获取所有的评分
        Wrapper<Grade> wrapper = new EntityWrapper<>();
        wrapper.eq("to_uid",userId);
        List<Grade> grades = this.selectList(wrapper);
        //遍历循环获取总分
        Long totalScore = 0L;
        for(Grade grade : grades){
            totalScore += grade.getScore();
        }
        //获取总分后取平均值
        long result = totalScore / grades.size();
        return result;
    }
}
