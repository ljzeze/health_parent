package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2021/1/13
 */
@Component("springSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户信息，包含角色与权限
        com.itheima.health.pojo.User dbUser = userService.findByUsername(username);
        if(null != dbUser){
            // 授权处理
            // 返回用户的信息 (用户名，密码，权限集合)
            //String username,  用户名
            //String password,  密码
            //Collection<? extends GrantedAuthority> authorities 用户的权限集合
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            // 授权用户的角色与权限
            Set<Role> userRoles = dbUser.getRoles();
            if(null != userRoles){
                for (Role role : userRoles) {
                    SimpleGrantedAuthority sgai = new SimpleGrantedAuthority(role.getKeyword());
                    // 授予角色
                    authorities.add(sgai);
                    // 角色下有权限
                    Set<Permission> permissions = role.getPermissions();
                    if(null != permissions){
                        for (Permission permission : permissions) {
                            // 授予权限
                            authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                        }
                    }
                }
            }
            // 如果密码是密文，就不能用noop, 用 bcrypt，或者配置文件中配置加载器
            User securityUser = new User(username, dbUser.getPassword(),authorities);
            return securityUser;
        }
        return null;
    }
}
