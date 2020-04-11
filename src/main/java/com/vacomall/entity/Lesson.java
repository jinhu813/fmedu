package com.vacomall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Lesson
 * @Description: TODO
 * @Author jimbo
 * @Date 2020/1/20 
 * @Version V1.0
 **/
@TableName("T_LESSON")
public class Lesson extends Model<Lesson> {

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 发布的信息的id */
    @TableField(value = "INFO_ID")
    private String infoId;

    /** 创建时间 */
    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    /** 名称 */
    @TableField(value = "LESSON_NAME")
    private String lessonName;

    /** 课程ID 课程字典的id */
    @TableField(value = "LESSON_ID")
    private String lessonId;

    /** 教师用户的id 教师用户的id */
    @TableField(value = "Teach_id")
    private String teachId;

    /** 教师用户名称 教师用户名称 */
    @TableField(value = "Teach_name")
    private String teachName;

    /** 学生用户id 学生用户id */
    @TableField(value = "STU_ID")
    private String stuId;

    /** 学生用户姓名 学生用户姓名 */
    @TableField(value = "STU_NAME")
    private String stuName;

    /** 申请状态 0,进行中，1，已完成 */
    private Long state;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setInfoId(String infoId)
    {
        this.infoId = infoId;
    }

    public String getInfoId()
    {
        return infoId;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setLessonName(String lessonName)
    {
        this.lessonName = lessonName;
    }

    public String getLessonName()
    {
        return lessonName;
    }
    public void setLessonId(String lessonId)
    {
        this.lessonId = lessonId;
    }

    public String getLessonId()
    {
        return lessonId;
    }
    public void setTeachId(String teachId)
    {
        this.teachId = teachId;
    }

    public String getTeachId()
    {
        return teachId;
    }
    public void setTeachName(String teachName)
    {
        this.teachName = teachName;
    }

    public String getTeachName()
    {
        return teachName;
    }
    public void setStuId(String stuId)
    {
        this.stuId = stuId;
    }

    public String getStuId()
    {
        return stuId;
    }
    public void setStuName(String stuName)
    {
        this.stuName = stuName;
    }

    public String getStuName()
    {
        return stuName;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("infoId", getInfoId())
                .append("createdTime", getCreatedTime())
                .append("lessonName", getLessonName())
                .append("lessonId", getLessonId())
                .append("teachId", getTeachId())
                .append("teachName", getTeachName())
                .append("stuId", getStuId())
                .append("stuName", getStuName())
                .toString();
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
