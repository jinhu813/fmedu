package com.vacomall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Apply;
import com.vacomall.entity.Information;
import com.vacomall.entity.Lesson;
import com.vacomall.entity.SysUser;
import com.vacomall.mapper.ApplyMapper;
import com.vacomall.service.ApplyService;
import com.vacomall.service.InformationService;
import com.vacomall.service.LessonService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @program:
 * @description: 申请 学员申请课程，教师申请学员课程Service业务层处理
 * @author: wangting
 * @create: 2020-01-21 09:02
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper,Apply> implements ApplyService {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private InformationService informationService;
    @Override
    public Page<Apply> selectApplyByPage(Apply apply, Page<Apply> page) {
        //构建查询条件,这是使用了Mybatis-plus，所以要新建一个Mybatis-plus查询用到的
        Wrapper<Apply> wrapper = new EntityWrapper<>();
        wrapper.eq("1","1");
        //如果id不为空，则查询条件中添加id
        if(!StringUtils.isEmpty(apply.getId())){
            wrapper.and().eq("ID",apply.getId());
        }
        //如果applyUserName不为空则查询条件添加applyUserName
        if(!StringUtils.isEmpty(apply.getApplyUserName())){
            wrapper.and().like("APPLY_USER_NAME",apply.getApplyUserName());
        }

        if(!StringUtils.isEmpty(apply.getApplyUserId())){
            wrapper.and().eq("APPLY_USER_ID",apply.getApplyUserId());
        }
        if(!StringUtils.isEmpty(apply.getPublishId())){
            wrapper.and().eq("publish_id",apply.getPublishId());
        }

        if(!StringUtils.isEmpty(apply.getState())){
            wrapper.and().eq("STATE",apply.getState());
        }

        if(!StringUtils.isEmpty(apply.getInfoTitle())){
            wrapper.and().like("info_title",apply.getInfoTitle());
        }
        return this.selectPage(page,wrapper);
    }

    @Override
    public List<Apply> selectApplyList(Apply Apply) {
        return null;
    }

    @Override
    @Transactional
    public Boolean insertApply(Apply apply) {
        return this.insert(apply);
    }

    @Override
    public boolean updateApply(Apply apply) {
        boolean b = this.updateById(apply);
        return b;
    }

    @Override
    public boolean deleteApplyByIds(List<String> ids) {
        boolean b = this.deleteBatchIds(ids);
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Information information, SysUser applyUser) {
        try {
            Apply apply = new Apply();
            String informationId = information.getId();
            //设置发布的信息id
            apply.setInfoId(informationId);
            //设置发布的信息的标题
            apply.setInfoTitle(information.getTitle());
            //设置信息的发布人
            apply.setPublishId(information.getCreatedUserId());
            apply.setPublishName(information.getCreatedUserName());
            //设置申请人的信息
            apply.setApplyUserId(applyUser.getId());
            apply.setApplyUserName(applyUser.getUserName());
            apply.setApplyTime(new Date());

            apply.setType(0L);
            apply.setState(0L);

            return this.insert(apply);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 申请审批
     * @param apply
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean doApproval(Apply apply) {
        //先获取apply
        Apply applyDb = this.selectById(apply.getId());
        //获取审批结果和审批意见
        Long state = apply.getState();
        String approvalDetail = apply.getApprovalDetail();
        //拷贝信息
        BeanUtils.copyProperties(applyDb,apply);
        //设置审批结果和审批意见
        applyDb.setState(state);
        applyDb.setApprovalDetail(approvalDetail);
        //更新
        boolean updateApply = this.updateApply(applyDb);
        //审批成功则需要生成对应的课程
        Lesson lesson = new Lesson();
        lesson.setCreatedTime(new Date());
        lesson.setState(0L);
        lesson.setInfoId(applyDb.getInfoId());
        lesson.setLessonName(applyDb.getInfoTitle());
        Information information = informationService.selectById(apply.getInfoId());
        lesson.setLessonId(information.getLessonTypeId());
        //判断角色
        /*
        * 如果是教师用户则设置自己为lesson的教师，否则是学生
        * */
        //判断是不是学生
        SysUser curentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        boolean isStudent = SecurityUtils.getSubject().hasRole("STUDENT");
        if(isStudent){
            //是学生的话就设置lesson的stuID为当前用户id
            lesson.setStuId(curentUser.getId());
            lesson.setStuName(curentUser.getUserName());
            //设置apply的（申请者id）为TeachId
            lesson.setTeachId(apply.getApplyUserId());
            lesson.setTeachName(apply.getApplyUserName());
            //如果是学生的话，一条招聘被申请了则不能再被申请
            if(state.equals(2L)){
                information.setState(2L);
                informationService.updateById(information);
                //本条申请通过，对于学生的同一条招聘信息其他的申请要全部不通过
                Wrapper<Apply> wrapper = new EntityWrapper<>();
                wrapper.eq("INFO_ID",information.getId());
                wrapper.and().ne("id",applyDb.getId());
                List<Apply> applies = this.selectList(wrapper);
                //非空判断
                if(!CollectionUtils.isEmpty(applies)){
                    for (Apply app : applies){
                        app.setState(1L);
                    }
                    this.updateBatchById(applies);
                }
            }

        }else{
            //是教师设置lesson的教师id为当前用户id
            lesson.setTeachName(curentUser.getUserName());
            lesson.setTeachId(curentUser.getId());
            lesson.setStuId(apply.getApplyUserId());
            lesson.setStuName(apply.getApplyUserName());
        }
        //保存lesson
        boolean saveLesson = lessonService.insert(lesson);
        return updateApply && saveLesson;
    }

}
