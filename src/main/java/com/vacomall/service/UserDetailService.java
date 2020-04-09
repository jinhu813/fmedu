package com.vacomall.service;

import com.baomidou.mybatisplus.service.IService;
import com.vacomall.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @program:
 * @description: 用户信息扩展
 * @author: wangting
 * @create: 2020-01-27 09:11
 */
public interface UserDetailService extends IService<UserDetail> {
    /**
     * 获取用户更多的信息
     * 如评论数，课程数等
     * @param userId
     * @return
     */
    Map<String,Object> getUserMoreInfo(String userId);
}
