package com.vacomall.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Collect;
import com.vacomall.entity.SysUser;
import com.vacomall.service.CollectService;
import com.vacomall.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;


/**
 * @program: fmedu
 * @description: 收藏
 * @author: liruihui
 * @create: 2020-03-14 19:14
 */
@Controller
@RequestMapping("/system/collect")
public class CollectController extends SuperController {
    private String prefix = "system/collect/";
    @Autowired
    private CollectService collectService;

    /**
     * 获取我的收藏
     *
     * @param model
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(Model model, @PathVariable("pageNo") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String userId = user.getId();
        Page<Collect> page = this.getPage(pageNo, pageSize);
        Wrapper wrapper = new EntityWrapper<Collect>();
        wrapper.eq("sid", userId);
        Page result = collectService.selectPage(page, wrapper);
        model.addAttribute("pageData", result);
        return prefix + "/list";
    }

    @Autowired
    private ISysUserService userService;

    @RequestMapping("/add")
    @ResponseBody
    public Rest add(String tid) {
        //获取当前用户
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //获取当前用户的id
        String sid = user.getId();

        SysUser tuser = userService.selectById(tid);
        if(null == tuser){
            return Rest.failure("被收藏用户不存在");
        }
        Wrapper wrapper = new EntityWrapper<Collect>();
        wrapper.eq("sid", sid);
        wrapper.and().eq("tid", tid);
        List list = collectService.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            Collect collect = new Collect();
            collect.setCreateTime(new Date());
            collect.setSid(sid);
            collect.setTid(tid);
            collect.setTname(tuser.getUserName());

            collectService.insert(collect);
        } else {
            return Rest.failure("收藏失败，用户已被收藏");
        }

        return Rest.failure("收藏成功");
    }

    @RequestMapping("/del")
    @ResponseBody
    public Rest del(String id) {
        collectService.deleteById(id);
        return Rest.ok("取消收藏成功");
    }
}
