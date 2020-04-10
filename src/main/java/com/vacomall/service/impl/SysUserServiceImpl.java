package com.vacomall.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vacomall.entity.Dict;
import com.vacomall.service.DictService;
import com.vacomall.service.DictTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.common.util.ShiroUtil;
import com.vacomall.entity.SysUser;
import com.vacomall.entity.SysUserRole;
import com.vacomall.mapper.SysUserMapper;
import com.vacomall.mapper.SysUserRoleMapper;
import com.vacomall.service.ISysUserService;
import org.springframework.util.CollectionUtils;

/**
 *
 * SysUser 表数据服务层接口实现类
 *
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	@Autowired private SysUserMapper userMapper;
	
	@Autowired private SysUserRoleMapper userRoleMapper;
	@Autowired
	private DictService dictService;
	@Autowired
	private DictTypeService dictTypeService;
	
	@Override
	public void insertUser(SysUser user, String[] roleIds) {
		// TODO Auto-generated method stub
		user.setCreateTime(new Date());
    	user.setPassword(ShiroUtil.md51024Pwd(user.getPassword(), user.getUserName()));
		initUserAddress(user);
		//保存用户
    	userMapper.insert(user);
		//绑定角色
		if(ArrayUtils.isNotEmpty(roleIds)){
			for(String rid : roleIds){
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(user.getId());
				sysUserRole.setRoleId(rid);
				userRoleMapper.insert(sysUserRole);
			}
		}
		
	}

	private void initUserAddress(SysUser user) {
		if(StringUtils.isNotEmpty(user.getProvince())){
			List<Dict> dicts = dictService.selectList("provinces", user.getProvince());
			StringBuilder sb = new StringBuilder();
			if(!CollectionUtils.isEmpty(dicts)){
				Optional<Dict> first = dicts.stream().findFirst();
				if(first.isPresent()){
					Dict dict = first.get();
					sb.append(dict.getName());
					if(StringUtils.isNotEmpty(user.getCity())){
						List<Dict> cityDict = dictService.selectList(dict.getValue(), user.getCity());
						if(!CollectionUtils.isEmpty(cityDict)){
							Optional<Dict> dict1 = cityDict.stream().findFirst();
							if(dict1.isPresent()){
								Dict city = dict1.get();
								sb.append(city.getName());
								if(StringUtils.isNotEmpty(user.getArea())){
									List<Dict> areaDict = dictService.selectList(city.getValue(), user.getArea());
									if(!CollectionUtils.isEmpty(areaDict)){
										Optional<Dict> areaFirst = areaDict.stream().findFirst();
										if(areaFirst.isPresent()){
											Dict area = areaFirst.get();
											sb.append(area.getName());
										}
									}
								}
							}
						}
					}
				}
			}
			user.setAddress(sb.toString());
		}
	}

	@Override
	public void updateUser(SysUser sysUser, String[] roleIds) {
		// TODO Auto-generated method stub
		sysUser.setPassword(null);
		//更新用户
		userMapper.updateById(sysUser);
		//删除已有权限
		userRoleMapper.delete(new EntityWrapper<SysUserRole>().eq("userId",sysUser.getId()));
		//重新绑定角色
		if(ArrayUtils.isNotEmpty(roleIds)){
			for(String rid : roleIds){
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(sysUser.getId());
				sysUserRole.setRoleId(rid);
				userRoleMapper.insert(sysUserRole);
			}
		}
	}

	@Override
	public Page<Map<Object, Object>> selectUserPage(Page<Map<Object, Object>> page, String search) {
		// TODO Auto-generated method stub
		page.setRecords(baseMapper.selectUserList(page, search));
		return page;
	}

	@Override
	public Page<Map<Object, Object>> selectUserPage(Page<Map<Object, Object>> page, SysUser sysUser) {
		page.setRecords(baseMapper.selectUserList2(page,sysUser));
		return page;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		this.deleteById(id);
		userRoleMapper.delete(new EntityWrapper<SysUserRole>().addFilter("userId = {0}", id));
	}


}