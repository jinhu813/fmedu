package com.vacomall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @program: fmedu
 * @description: 预约
 * @author:
 * @create: 2020-03-14 19:54
 */
@TableName("t_RESERVED")
public class Reserved {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField(value = "CREATED_BY")
    private String createdBy;

    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    @TableField("for_id")
    private String forId;

    @TableField("lesson_id")
    private String lessonId;

    @TableField("lesson_name")
    private String lessonName;

    @TableField("begin_time")
    private Date begin;
    @TableField("end_time")
    private Date end;

    @TableField("for_name")
    private String forName;
    @TableField("CREATED_id")
    private String createdId;

    private Integer state = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getForName() {
        return forName;
    }

    public void setForName(String forName) {
        this.forName = forName;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }
}
