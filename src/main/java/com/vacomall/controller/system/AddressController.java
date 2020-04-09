package com.vacomall.controller.system;

import com.vacomall.common.bean.Rest;
import com.vacomall.common.controller.SuperController;
import com.vacomall.entity.Dict;
import com.vacomall.service.DictService;
import com.vacomall.service.DictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 根据省份的code获取下面的市
     * @param provinceCode
     * @return
     */
    @GetMapping("/city/provinceCode")
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

    private void dictCovert(List<Map<String, String>> listMap, List<Dict> dicts) {
        if(!CollectionUtils.isEmpty(dicts)){
            for (Dict dict : dicts){
                Map<String,String> map = new HashMap<>();
                map.put("id",dict.getName());
                map.put("text",dict.getName());
                listMap.add(map);
            }
        }else{
            Map<String,String> map = new HashMap<>();
            map.put("id","");
            map.put("text","--请选择--");
            listMap.add(map);
        }
    }
}
