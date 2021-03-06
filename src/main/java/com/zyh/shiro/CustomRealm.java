package com.zyh.shiro;


import com.zyh.entity.User;
import com.zyh.mapper.UserMapper;
import com.zyh.service.UserService;
import com.zyh.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * 安全框架用的shiro 要重写doGetAuthenticationInfo方法
 * 添加我们自己的规则
 */

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 使用此方法进行id正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        Integer userId = JWTUtil.getUserId(token);
        if(userId==null)
        {
            throw new UnknownAccountException();
        }
        else{
            boolean verify = JWTUtil.verify(token, userId);
            if(!verify)
            {
                throw new UnknownAccountException();
            }
        }

        return new SimpleAuthenticationInfo(token, token,this.getName());
    }

    /**
     * 权限认证
     * 暂时还没有涉及到权限业务
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId = JWTUtil.getUserId(principals.toString());
        User user = userMapper.findUserById(userId);
        if(user==null)
        {
            throw new UnknownAccountException();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(user.getUsername().equals("admin")){
            info.addRole("admin");
        }
        return info;
    }


}
