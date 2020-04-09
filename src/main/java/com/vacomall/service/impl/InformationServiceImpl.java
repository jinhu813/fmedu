package com.vacomall.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Information;
import com.vacomall.mapper.InformationMapper;
import com.vacomall.service.InformationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program:
 * @description: 发布的信息 招聘信息、求职信息Service业务层处理
 * @author: liruihui
 * @create: 2020-01-20 18:29
 */
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements InformationService {
    @Autowired
    private InformationMapper informationMapper;
    @Override
    public Information selectInformationById(String id) {
        return this.selectById(id);
    }

    @Override
    public List<Information> selectInformationList(Information information) {
        return null;
    }

    @Transactional
    @Override
    public boolean insertInformation(Information information) {
        return this.insert(information);
    }

    @Transactional
    @Override
    public boolean updateInformation(Information information) {
        boolean b = this.updateById(information);
        return b;
    }

    @Transactional
    @Override
    public boolean deleteInformationByIds(List<String> ids) {
        boolean b = this.deleteBatchIds(ids);
        return b;
    }

    @Override
    public boolean deleteInformationById(String id) {
        boolean b = this.deleteById(id);
        return b;
    }


    @Override
    public Page<Information> selectInfomationByPage(Page<Information> page, Information information) {
        EntityWrapper<Information> wrapper = new EntityWrapper<Information>();
        wrapper.eq("1", "1");
        if (StringUtils.isNotEmpty(information.getCreatedUserId())) {
            wrapper.and().eq("CREATED_USER_ID", information.getCreatedUserId());
        }

        if (StringUtils.isNotEmpty(information.getId())) {
            wrapper.and().eq("id", information.getId());
        }

        if (null != information.getState()) {
            wrapper.and().eq("state", information.getState());
        }

        if (StringUtils.isNotEmpty(information.getTitle())) {
            wrapper.and().like("title", information.getTitle());
        }
        wrapper.orderBy("CREATED_TIME",false);

        Page<Information> informationPage = this.selectPage(page, wrapper);
        return informationPage;
    }

    @Override
    public Page<Information> getInfoApplyPage(Page<Information> page,Information information) {
        page.setRecords(baseMapper.getInfoApply(page,information));
        return page;
    }


}
