package com.vacomall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.Apply;
import com.vacomall.entity.Information;
import com.vacomall.entity.SysUser;

import java.util.List;

/**
 * @program:
 * @description: 申请 学员申请课程，教师申请学员课程Service接口
 * @author: wangting
 * @create: 2020-01-21 09:01
 */
public interface ApplyService extends IService<Apply> {

    /**
     * 分页查询申请 学员申请课程，教师申请学员课程
     *
     * @return 申请 学员申请课程，教师申请学员课程
     */
    Page<Apply> selectApplyByPage(Apply apply,Page<Apply> page);

    /**
     * 查询申请 学员申请课程，教师申请学员课程列表
     *
     * @param Apply 申请 学员申请课程，教师申请学员课程
     * @return 申请 学员申请课程，教师申请学员课程集合
     */
    List<Apply> selectApplyList(Apply Apply);

    /**
     * 新增申请 学员申请课程，教师申请学员课程
     *
     * @param Apply 申请 学员申请课程，教师申请学员课程
     * @return 结果
     */
    Boolean insertApply(Apply Apply);

    /**
     * 修改申请 学员申请课程，教师申请学员课程
     *
     * @param Apply 申请 学员申请课程，教师申请学员课程
     * @return 结果
     */
    boolean updateApply(Apply Apply);

    /**
     * 批量删除申请 学员申请课程，教师申请学员课程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteApplyByIds(List<String> ids);

    boolean save(Information information, SysUser applyUser);

    boolean doApproval(Apply apply);

}
