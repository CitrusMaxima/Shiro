package realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm
{

    //设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    //用于认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //token是用户输入的
        //第一步:丛token中取出身份信息
        String userCode= (String) token.getPrincipal();

        //第二步:根据用户输入的userCode丛数据库查询

        //模拟从数据库查询到的密码
        String password="111111";


        //如果查不到返回null，

        //如果查询到，返回认证信息AuthenticationInfo

        SimpleAuthenticationInfo simpleAuthenticationInfo=new
                SimpleAuthenticationInfo(userCode,password,this.getName());


        return simpleAuthenticationInfo;
    }

    //用于授权，该功能在下篇文章中进行讲解
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //从principals获取主身份信息
        //将getPrimaryPrincipal方法返回值转为真实身份类型(在上边的goGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo)
        String userCode= (String) principals.getPrimaryPrincipal();

        //根据身份信息获取权限信息,
        //模拟从数据库中获取到的动态权限数据
        List<String> permissions=new ArrayList<>();
        permissions.add("user:create");//模拟user的创建权限
        permissions.add("items:add");//模拟商品的添加权限

        //查到权限数据，返回授权信息(包括上边的permissions)
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();

        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

}