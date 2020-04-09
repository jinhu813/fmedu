package com.vacomall.test.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.entity.Apply;
import com.vacomall.service.ApplyService;
import com.vacomall.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/**
 * @program: AdminLTE-admin
 * @description: ApplyService
 * @author: liruihui
 * @create: 2020-01-21 09:55
 */
public class ApplyServiceTest extends SpringTest {

    @Autowired
    private ApplyService applyService;

    @Test
    public void getApplyPage() {
        Apply apply = new Apply();
        apply.setId("30d89e33-fd41-4307-8e32-58f7ccc1d826");
        apply.setPublishId("pub");
        Page<Apply> page = new Page<>(1,2);
        Page<Apply> page1 = applyService.selectApplyByPage(apply, page);
        System.out.println(page1.getTotal());
    }

    @Test
    public void testSave(){
        for (int i = 0;i < 5;i++){
            Apply apply = new Apply();
            apply.setId(UUID.randomUUID().toString());
            apply.setApplyTime(new Date());
            apply.setApplyUserId(String.valueOf(i));
            apply.setApplyUserName("userName"+i);
            apply.setPublishId("pub"+i);
            apply.setState(new Long(0));
            apply.setInfoId("infoId"+i);
            applyService.insertApply(apply);
        }
    }
}
