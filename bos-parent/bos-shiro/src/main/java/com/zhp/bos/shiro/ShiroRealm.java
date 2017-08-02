package com.zhp.bos.shiro;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhp.bos.entity.auth.Function;
import com.zhp.bos.entity.auth.Role;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.service.base.FacadeService;

@Component
public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private FacadeService facadeService;

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// SimpleAuthorizationInfo用来封装角色和权限
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取用户
		Subject subject = SecurityUtils.getSubject();
		User exitUser = (User) subject.getPrincipal();
		// 超级管理员拥有所有角色以及权限,超管数据库是只有一个admin角色，但要访问所有资源，故需要所有角色
		if ("1054598852@qq.com".equalsIgnoreCase(exitUser.getEmail())) {
			List<Role> roles = facadeService.getRoleService().findAll();
			if (roles != null && roles.size() != 0) {
				for (Role role : roles) {
					info.addRole(role.getCode());
				}
			}
			List<Function> functions = facadeService.getFunctionService().findAll();
			if (functions != null && functions.size() != 0) {
				for (Function function : functions) {
					info.addStringPermission(function.getCode());
				}
			}
		} else {
			// 根据用户id去查询所有角色
			List<Role> roles = facadeService.getRoleService().findRoleByUserId(exitUser.getId());
			if (roles != null && roles.size() != 0) {
				for (Role role : roles) {
					info.addRole(role.getCode());
					Set<Function> functions = role.getFunctions();
					if (functions != null && functions.size() != 0) {
						for (Function function : functions) {
							info.addStringPermission(function.getCode());
						}
					}
				}
			}
		}
		return info;
	}

	/**
	 * 认证 AuthenticationToken：令牌对象
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		// 令牌对象强转
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		// 获取用户的用户名
		String email = token.getUsername();
		// 使用用户名查询数据库
		User exitUser = facadeService.getUserService().findUserByEmail(email);
		if (exitUser == null) {
			return null;
		}
		/**
		 * new SimpleAuthenticationInfo(principal, credentials, realmName)
		 * principal:用户信息，即查询出来的user credentials：证书，传递密码
		 * realmName：Realm的spring注册名
		 */
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(exitUser, exitUser.getPassword(), super.getName());
		return info;
	}

}
