package com.vacomall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.Collect;
import com.vacomall.mapper.CollectMapper;
import com.vacomall.service.CollectService;
import org.springframework.stereotype.Service;

/**
 * @program: fmedu
 * @description: 收藏
 * @author: liruihui
 * @create: 2020-03-14 19:13
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
}
