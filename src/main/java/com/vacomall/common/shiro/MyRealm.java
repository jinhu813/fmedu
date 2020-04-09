package com.vacomall.common.shiro;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.vacomall.entity.SysUser;
import com.vacomall.service.ISysRoleMenuService;
import com.vacomall.service.ISysUserRoleService;
import com.vacomall.service.ISysUserService;

/**
 * 根据shiro文档，必须写一个AuthorizingRealm的子类
 * 用于自定义用户登陆验证逻辑及对用户授权
 */
public class MyRealm extends AuthorizingRealm{
	
	/**
	 * 用户服务
	 */
	@Autowired private ISysUserService userService;
	/**
	 * 用户角色服务
	 */
	@Autowired private ISysUserRoleService sysUserRoleService;
	/**
	 * 角色菜单服务
	 */
	@Autowired private ISysRoleMenuService sysRoleMenuService;
	
	/**
	 * 用户登陆认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取当前用户的token信息
		UsernamePasswordToken user = (UsernamePasswordToken) token;
		//从数据库中查询用户信息
		SysUser sysUser = userService.selectOne(new EntityWrapper<SysUser>().eq("userName", user.getUsername()));

		//如果查不到就说明不存在用户，认证失败
		if(sysUser == null){
			throw new UnknownAccountException();
		}
		//用户是不是被锁定，被锁定的用户认证就会失败
		if(sysUser.getUserState() == SysUser._0){
			throw new LockedAccountException();
		}
		//用户名，数据库中的密码,reaml名称
		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(),sysUser.getPassword(),getName());
		//盐值加密
		ByteSource byteSource = ByteSource.Util.bytes(user.getUsername());
		//认证成功后，把用户信息封装成shiro框架需要的对象
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(),byteSource,getName());
		return info;
	}
	
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		//获取当前登陆的用户信息
		SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//根据用户id查询用户角色
		Set<String> roles = sysUserRoleService.findRolesByUid(sysUser.getId());
		//根据用户id查询用户所有的权限
		Set<String> permissions = sysRoleMenuService.findMenusByUid(sysUser.getId());
		//设置用户角色
		info.setRoles(roles);
		//设置用户权限
		info.setStringPermissions(permissions);
		return info;
	}

}
