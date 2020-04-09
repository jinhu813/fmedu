package com.vacomall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * @ClassName Apply
 * @Description: 申请 学员申请课程，教师申请学员课程对象 T_APPLY
 * @Author wangting
 * @Date 2020/1/20 
 * @Version V1.0
 **/
@TableName("T_APPLY")
public class Apply {
    /** 申请人 */
    @TableField("APPLY_USER_ID")
    private String applyUserId;

    /** 申请时间 */
    @TableField("APPLY_TIME")
    private Date applyTime;

    /** 申请人名称 */
    @TableField("APPLY_USER_NAME")
    private String applyUserName;

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 发布的信息id */
    @TableField("INFO_ID")
    private String infoId;

    /** 申请状态 0,申请中，1，未通过2，通过 */
    private Long state;

    @TableField("info_title")
    private String infoTitle;

    private Long type;

    @TableField("approval_detail")
    private String approvalDetail;

    /**
     * 信息发布人id
     */
    @TableField(value = "publish_id")
    private String publishId;

    @TableField(value = "publish_name")
    private String publishName;

    public void setApplyUserId(String applyUserId)
    {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserId()
    {
        return applyUserId;
    }
    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }
    public void setApplyUserName(String applyUserName)
    {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserName()
    {
        return applyUserName;
    }
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
    public void setState(Long state)
    {
        this.state = state;
    }

    public Long getState()
    {
        return state;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getApprovalDetail() {
        return approvalDetail;
    }

    public void setApprovalDetail(String approvalDetail) {
        this.approvalDetail = approvalDetail;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applyUserId", getApplyUserId())
                .append("applyTime", getApplyTime())
                .append("applyUserName", getApplyUserName())
                .append("id", getId())
                .append("infoId", getInfoId())
                .append("state", getState())
                .toString();
    }
}
