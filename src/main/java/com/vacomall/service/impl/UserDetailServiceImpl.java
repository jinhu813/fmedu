package com.vacomall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Grade;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.SysUser;
import com.vacomall.entity.UserDetail;
import com.vacomall.mapper.UserDetailMapper;
import com.vacomall.service.GradeService;
import com.vacomall.service.LessonService;
import com.vacomall.service.UserDetailService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program:
 * @description: 用户信息扩展
 * @author: wangting
 * @create: 2020-01-27 09:12
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements UserDetailService {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private GradeService gradeService;
    @Override
    public Map<String, Object> getUserMoreInfo(String userId) {
        Map<String, Object> resultMap = new HashMap<>(2);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //判断角色
        boolean isStudent = SecurityUtils.getSubject().hasRole("STUDENT");
        Wrapper lessonWrapper = new EntityWrapper<Lesson>();
        int lessonCount = 0;
        //如果是学生
        if(isStudent) {
            lessonWrapper.eq("STU_ID", user.getId());
//            lessonWrapper.groupBy("STU_ID");
//            List list = lessonService.selectList(lessonWrapper);
            lessonCount = lessonService.selectCount(lessonWrapper);
        }else{
            lessonWrapper.eq("Teach_id",user.getId());
            lessonWrapper.groupBy("LESSON_ID");
            List list = lessonService.selectList(lessonWrapper);
            lessonCount = list.size();
        }
        resultMap.put("lessons",lessonCount);
        //获取评论数
        Wrapper<Grade> gradeWrapper = new EntityWrapper<>();
        gradeWrapper.eq("to_uid",user.getId());
        int gradeConut = gradeService.selectCount(gradeWrapper);
        resultMap.put("grades",gradeConut);
        return resultMap;
    }
}
