package com.vacomall.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.Information;

import java.util.List;

/**
 *
 */
public interface InformationService extends IService<Information> {

    /**
     * 查询发布的信息 招聘信息、求职信息
     *
     * @param id 发布的信息 招聘信息、求职信息ID
     * @return 发布的信息 招聘信息、求职信息
     */
    Information selectInformationById(String id);

    /**
     * 查询发布的信息 招聘信息、求职信息列表
     *
     * @param Information 发布的信息 招聘信息、求职信息
     * @return 发布的信息 招聘信息、求职信息集合
     */
    List<Information> selectInformationList(Information Information);

    /**
     * 新增发布的信息 招聘信息、求职信息
     *
     * @param Information 发布的信息 招聘信息、求职信息
     * @return 结果
     */
    boolean insertInformation(Information Information);

    /**
     * 修改发布的信息 招聘信息、求职信息
     *
     * @param Information 发布的信息 招聘信息、求职信息
     * @return 结果
     */
    boolean updateInformation(Information Information);

    /**
     * 批量删除发布的信息 招聘信息、求职信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    boolean deleteInformationByIds(List<String> ids);

    /**
     * 删除发布的信息 招聘信息、求职信息信息
     *
     * @param id 发布的信息 招聘信息、求职信息ID
     * @return 结果
     */
    boolean deleteInformationById(String id);

    Page<Information> selectInfomationByPage(Page<Information> page, Information information);

    Page<Information> getInfoApplyPage(Page<Information> page,Information information);

}
