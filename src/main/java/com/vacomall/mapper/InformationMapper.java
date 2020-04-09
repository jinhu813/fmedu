package com.vacomall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.entity.Information;

import java.util.List;
import java.util.Map;


/**
 * @program: AdminLTE-admin
 * @description: 发布的信息 招聘信息、求职信息Mapper接口
 * @author: liruihui
 * @create: 2020-01-20 18:33
 */
public interface InformationMapper extends BaseMapper<Information> {
    List<Information> getInfoApply(Page<Information> page, Information information);
}
