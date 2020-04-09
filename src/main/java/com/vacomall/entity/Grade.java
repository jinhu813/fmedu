package com.vacomall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * @program: AdminLTE-admin
 * @description: 用户评分实体类
 * @author: wangting
 * @create: 2020-02-15 09:31
 */

@TableName("t_grade")
public class Grade {
    /** id 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 评价的类型。1学生，2，教师 */
    private Long type;

    /** 分数 */
    private Long score;

    /** 具体内容 */
    private String detail;

    /** 课程id */
    @TableField("lesson_id ")
    private String lessonId;

    /** 课程名称 */
    @TableField("lesson_name ")
    private String lessonName;

    /** 评价人id */
    @TableField("from_uid ")
    private String fromUid;
    @TableField("from_uname")
    private String fromUname;

    /** 被评价人id */
    @TableField("to_uid ")
    private String toUid;

    @TableField("create_time ")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setScore(Long score)
    {
        this.score = score;
    }

    public Long getScore()
    {
        return score;
    }
    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getDetail()
    {
        return detail;
    }
    public void setLessonId(String lessonId)
    {
        this.lessonId = lessonId;
    }

    public String getLessonId()
    {
        return lessonId;
    }
    public void setLessonName(String lessonName)
    {
        this.lessonName = lessonName;
    }

    public String getLessonName()
    {
        return lessonName;
    }
    public void setFromUid(String fromUid)
    {
        this.fromUid = fromUid;
    }

    public String getFromUid()
    {
        return fromUid;
    }
    public void setToUid(String toUid)
    {
        this.toUid = toUid;
    }

    public String getToUid()
    {
        return toUid;
    }

    public String getFromUname() {
        return fromUname;
    }

    public void setFromUname(String fromUname) {
        this.fromUname = fromUname;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("createTime", getCreateTime())
                .append("type", getType())
                .append("score", getScore())
                .append("detail", getDetail())
                .append("lessonId", getLessonId())
                .append("lessonName", getLessonName())
                .append("fromUid", getFromUid())
                .append("toUid", getToUid())
                .toString();
    }
}
