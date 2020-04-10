package com.vacomall.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Dict;
import com.vacomall.entity.SysUser;
import com.vacomall.service.DictService;
import com.vacomall.service.DictTypeService;
import com.vacomall.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: fmedu
 * @description: 区划加载
 * @author:
 * @create: 2020-04-09 20:58
 */
@Controller
@RequestMapping("/address")
public class AddressController extends SuperController {

    @Autowired
    private DictService dictService;
    @Autowired
    private DictTypeService dictTypeService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 根据省份的code获取下面的市
     * @param provinceCode
     * @return
     */
    @GetMapping("/city/provinceCode/{provinceCode}")
    @ResponseBody
    public Rest getCity(@PathVariable String provinceCode){
        List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
        if(StringUtils.isNotEmpty(provinceCode)){
            List<Dict> dicts = dictService.findByTypeCode(provinceCode);
            dictCovert(listMap, dicts);
            return Rest.okData(listMap);
        }
        return Rest.okData(listMap);
    }

    /**
     * 根据省份的code获取下面的市
     * @param cityCode
     * @return
     */
    @GetMapping("/city/area/{cityCode}")
    @ResponseBody
    public Rest getArea(@PathVariable String cityCode){
        List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
        if(StringUtils.isNotEmpty(cityCode)){
            List<Dict> dicts = dictService.findByTypeCode(cityCode);
            dictCovert(listMap, dicts);
            return Rest.okData(listMap);
        }
        return Rest.okData(listMap);
    }

    @RequestMapping("/teacher/list/{pageNumber}")
    public String teachers(@PathVariable Integer pageNumber, @RequestParam(defaultValue="15") Integer pageSize, String search,
                           Model model,String province,String city,String area){
        SysUser sysUser = new SysUser();
        if(StringUtils.isNotBlank(search)){
            model.addAttribute("search", search);
            sysUser.setUserName(search);
        }
        if(StringUtils.isNotEmpty(province)){
            model.addAttribute("province",province);
            sysUser.setProvince(province);
            List<Dict> cities = dictService.findByTypeCode(province);
            model.addAttribute("cities",cities);
        }
        if(StringUtils.isNotEmpty(city)){
            model.addAttribute("city",city);
            sysUser.setCity(city);
            List<Dict> areas = dictService.findByTypeCode(city);
            model.addAttribute("areas",areas);
        }
        if(StringUtils.isNotEmpty(area)){
            sysUser.setArea(area);
            model.addAttribute("area",area);
        }
        List<Dict> provinces = dictService.findByTypeCode("provinces");
        model.addAttribute("provinces",provinces);
        Page<Map<Object, Object>> page = getPage(pageNumber,pageSize);
        model.addAttribute("pageSize", pageSize);
        Page<Map<Object, Object>> pageData = sysUserService.selectUserPage(page, sysUser);
        model.addAttribute("pageData", pageData);
        return "/system/user/teacherList";
    }
    private void dictCovert(List<Map<String, String>> listMap, List<Dict> dicts) {
        if(!CollectionUtils.isEmpty(dicts)){
            Map<String,String> map1 = new HashMap<>();
            map1.put("id","");
            map1.put("text","");
            listMap.add(map1);
            for (Dict dict : dicts){
                Map<String,String> map = new HashMap<>();
                map.put("id",dict.getValue());
                map.put("text",dict.getName());
                listMap.add(map);
            }
        }else{
            Map<String,String> map = new HashMap<>();
            map.put("id","");
            map.put("text","");
            listMap.add(map);
        }
    }
}
