package com.vacomall.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vacomall.entity.SysRole;
import com.vacomall.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.vacomall.entity.SysUserRole;
import com.vacomall.mapper.SysUserRoleMapper;
import com.vacomall.service.ISysUserRoleService;

/**
 *
 * SysUserRole 表数据服务层接口实现类
 *
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

	@Autowired
	private ISysRoleService roleService;
	@Override
	public Set<String> findRolesByUid(String uid) {
		// TODO Auto-generated method stub
		List<SysUserRole> list = this.selectList(new EntityWrapper<SysUserRole>().eq("userId", uid));

		List<String> set = new ArrayList<>();
		for (SysUserRole ur : list) {
			set.add(ur.getRoleId());
		}
		List<SysRole> sysRoles = roleService.selectBatchIds(set);
		Set<String> roleSet = new HashSet<>(sysRoles.size());
		for(SysRole role : sysRoles){
			roleSet.add(role.getRoleName());
		}
		return roleSet;
	}
}