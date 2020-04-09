package com.vacomall.entity.vo;

import com.vacomall.entity.Lesson;

/**
 * @program: AdminLTE-admin
 * @description: 带评分信息的课程
 * @author: wangting
 * @create: 2020-02-15 10:08
 */
public class LessonVo extends Lesson {

    /** 分数 */
    private Long score;

    /** 具体内容 */
    private String detail;

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
