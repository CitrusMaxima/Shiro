package shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Test;

import java.util.Arrays;

public class AuthenticationTest
{
    //用户的登录和退出
    @Test
    public void testLoginAndLogout()
    {
        //创建securityManager工厂
        Factory<SecurityManager> factory=new IniSecurityManagerFactory(
                "classpath:shiro-first.ini");

        /* 创建SecurityManager */
        SecurityManager securityManager = factory.getInstance();

        //将securityManager设置到当前的运行环境中
        SecurityUtils.setSecurityManager(securityManager);

        //丛SecurityUtils里边的到一个subject
        Subject subject = SecurityUtils.getSubject();

        // 在认证提交前准备token（令牌）
        // 模拟用户输入的账号和密码，将来是由用户输入进去从页面传送过来
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan",
                "111111");

        try {
            // 执行认证提交
            subject.login(token);
        } catch (AuthenticationException e) {
            // 认证失败
            e.printStackTrace();
        }

        // 是否认证通过
        boolean isAuthenticated = subject.isAuthenticated();

        System.out.println("是否认证通过：" + isAuthenticated);

        // 退出操作
        subject.logout();
    }

    @Test
    public void testCustomRealm()
    {
        //创建securityManager工厂
        Factory<SecurityManager> factory=new IniSecurityManagerFactory(
                "classpath:shiro-realm.ini");

        //创建SecurityManager
        SecurityManager securityManager = factory.getInstance();

        //将securityManager设置到当前的运行环境汇中
        SecurityUtils.setSecurityManager(securityManager);

        //丛SecurityUtils里边创建一个
        Subject subject = SecurityUtils.getSubject();

        // 在认证提交前准备token（令牌）
        // 这里的账号和密码 将来是由用户输入进去
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan",
                "111111");

        try {
            // 执行认证提交
            subject.login(token);
        } catch (AuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 是否认证通过
        boolean isAuthenticated = subject.isAuthenticated();

        System.out.println("是否认证通过：" + isAuthenticated);

    }

    @Test
    public void testAuthorization()
    {
        //第一步，创建SecurityManager工厂
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-permission.ini");

        //第二步:创建SecurityManager
        SecurityManager securityManager=factory.getInstance();

        //第三步，将SecurityManager设置到系统运行环境，和spring整合后会将SecurityManager配置到spring容器中，一般单例管理
        SecurityUtils.setSecurityManager(securityManager);

        //第四步，创建subject
        Subject subject=SecurityUtils.getSubject();

        //创建token令牌,这里的用户名和密码以后由用户输入
        UsernamePasswordToken token=new UsernamePasswordToken("zhangsan","123");

        try {
            //执行认证，将用户输入的信息同数据库(即.ini配置文件)中信息进行对比
            subject.login(token);
        }catch (AuthenticationException e)
        {
            e.printStackTrace();
        }

        System.out.println("认证状态:"+subject.isAuthenticated());

        //认证通过后才能执行授权
        //第一种授权方式是基于角色的授权,hasRole传入角色的标识
        boolean ishasRole=subject.hasRole("role1");//该用户是否有role1这个角色
        System.out.println("单个角色判断"+ishasRole);

        //hasAllRoles是否拥有多个角色
        boolean hasAllRoles=subject.hasAllRoles(Arrays.asList("role1","role2"));
        System.out.println("多个角色判断"+hasAllRoles);//角色的就讲到这里了，后面我们都是通过资源进行权限讲解


        //使用check方法进行授权，如果授权不通过会抛出异常
        //subject.checkRole("role3");

        //第二种授权方式是基于资源的授权,isPermitted传入权限标识符
        boolean isPermitted=subject.isPermitted("user:create");//该用户是否有对user资源进行创建的权限
        System.out.println("单个权限判断"+isPermitted);

        //多个权限判断
        boolean isPermittedAll=subject.isPermittedAll("user:create:1","user:update");
        System.out.println("多个权限判断:"+isPermittedAll);

    }

    @Test
    public void testAuthorizationCustomRealm()
    {
        //第一步，创建SecurityManager工厂
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-realm.ini");

        //第二步:创建SecurityManager
        SecurityManager securityManager=factory.getInstance();

        //第三步，将SecurityManager设置到系统运行环境，和spring整合后会将SecurityManager配置到spring容器中，一般单例管理
        SecurityUtils.setSecurityManager(securityManager);

        //第四步，创建subject
        Subject subject=SecurityUtils.getSubject();

        //创建token令牌,这里的用户名和密码以后由用户输入
        UsernamePasswordToken token=new UsernamePasswordToken("zhangsan","111111");

        try {
            //执行认证，将用户输入的信息同数据库(即.ini配置文件)中信息进行对比
            subject.login(token);
        }catch (AuthenticationException e)
        {
            e.printStackTrace();
        }

        System.out.println("认证状态:"+subject.isAuthenticated());



        //基于资源的授权,调用isPermitted方法会调用CustomRealm从数据库查询正确的权限数据
        // isPermitted传入权限标识符，判断user:create:1是否在CustomRealm查询到的权限数据之内
        boolean isPermitted=subject.isPermitted("user:create:1");//该用户是否有对user的1资源进行创建的权限
        System.out.println("单个权限判断"+isPermitted);

        //多个权限判断
        boolean isPermittedAll=subject.isPermittedAll("user:create:1","user:create");
        System.out.println("多个权限判断:"+isPermittedAll);
    }
}