package com.vacomall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * @ClassName Information
 * @Description: 发布的信息 招聘信息、求职信息对象 t_information
 * @Author jimbo
 * @Date 2020/1/20 
 * @Version V1.0
 **/
@TableName("t_information")
public class Information {

    @TableField("CREATED_USER_ID")
    /** 创建人id 用户id */
    private String createdUserId;

    /** 状态 0，未审核，1，已审核，可申请，2，已被申请或失效 */
    private Long state;

    /** 创建人姓名 */
    @TableField("CREATED_USER_NAME")
    private String createdUserName;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /** 标题 */
    private String title;

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 类型 1,招聘，2，求职 */
    private Long type;

    /** 课程类型 课程类型id */
    @TableField("LESSON_TYPE_ID")
    private String lessonTypeId;

    /** 课程类型名称 课程类型名称 */
    @TableField("LESSON_TYPE_NAME")
    private String lessonTypeName;

    /** 信息内容 */
    private String content;

    /**
     * 审批意见
     */
    @TableField("DENY_RESON")
    private String denyReson;

    public String getDenyReson() {
        return denyReson;
    }

    public void setDenyReson(String denyReson) {
        this.denyReson = denyReson;
    }

    public void setCreatedUserId(String createdUserId)
    {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserId()
    {
        return createdUserId;
    }
    public void setState(Long state)
    {
        this.state = state;
    }

    public Long getState()
    {
        return state;
    }
    public void setCreatedUserName(String createdUserName)
    {
        this.createdUserName = createdUserName;
    }

    public String getCreatedUserName()
    {
        return createdUserName;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
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
    public void setLessonTypeId(String lessonTypeId)
    {
        this.lessonTypeId = lessonTypeId;
    }

    public String getLessonTypeId()
    {
        return lessonTypeId;
    }
    public void setLessonTypeName(String lessonTypeName)
    {
        this.lessonTypeName = lessonTypeName;
    }

    public String getLessonTypeName()
    {
        return lessonTypeName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("createdUserId", getCreatedUserId())
                .append("state", getState())
                .append("createdUserName", getCreatedUserName())
                .append("createdTime", getCreatedTime())
                .append("updatedTime", getUpdatedTime())
                .append("title", getTitle())
                .append("id", getId())
                .append("type", getType())
                .append("lessonTypeId", getLessonTypeId())
                .append("lessonTypeName", getLessonTypeName())
                .append("content", getContent())
                .toString();
    }
}
