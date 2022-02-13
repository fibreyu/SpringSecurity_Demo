package fun.fibreyu.springsecurity.config;

import fun.fibreyu.springsecurity.handler.MyAccessDeniedHandler;
import fun.fibreyu.springsecurity.handler.MyAuthenticationFailureHandler;
import fun.fibreyu.springsecurity.handler.MyAuthenticationSuccessHandler;
import fun.fibreyu.springsecurity.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author fibreyu
 * @since 1.0.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 创建PasswordEncoder容器给spring管理
     * @return
     */
    @Bean
    public PasswordEncoder getPwd() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单提交
        http.formLogin()
                // 自定义用户名参数
                // .usernameParameter("my_username")
                // 自定义密码参数
                // .passwordParameter("my_password")
                // 自定义登录页面
                .loginPage("/login.html")
                // 参数必须和表单提交的接口一致,使login接口执行自定义登录逻辑
                // 否则默认接口不走自定义登录逻辑,还是会被拦截
                .loginProcessingUrl("/login")
                // 登录成功后跳转页面,必须使post请求
                //.successForwardUrl("/toMain")
                // 指向自定义的成功跳转handler
                .successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                // 登录失败的跳转页面,同样需要post请求
                //.failureForwardUrl("/toError");
                // 指向自定义的失败跳转handler
                .failureHandler(new MyAuthenticationFailureHandler("/error.html"));

        // 授权
        http.authorizeRequests()
                // 放行登录失败页面
                .antMatchers("/error.html").permitAll()
                // 放行登录页,否则登录页需要认证的话会一直循环跳转到登录页
                .antMatchers("/login.html").permitAll()
                .antMatchers("login.html").access("permitAll")
                .antMatchers("/css/**", "/js/**", "/image/**").permitAll()
                .antMatchers("/**/*.png").permitAll()
                .antMatchers("/showLogin").permitAll()
                .regexMatchers(".+[.]png").permitAll()
                .regexMatchers(HttpMethod.POST, "/demo").permitAll()
                .mvcMatchers("/demo").servletPath("/fibreyu").permitAll()
                //基于权限
                //.antMatchers("/main1.html").hasAuthority("admin")
                //.antMatchers("main1.html").hasAnyAuthority("aDmin", "admiN")
                // 基于劫色
                .antMatchers("main1.html").hasRole("abc")
                //.antMatchers("main1.html").access("hasRole('abc')")
                //.antMatchers("main1.html").hasAnyRole("abc", "Abc")
                // 基于IP地址
                //.antMatchers("main1.html").hasIpAddress("127.0.0.1")
                // 所有的请求都必须认证，才能访问,anyRequest必须放最后
                //.anyRequest().authenticated()
                .anyRequest().access("@myserviceImpl.hasPermission(request, authentication)");

        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        // 记住我
        http.rememberMe()
                // 设置数据源
                .tokenRepository(persistentTokenRepository)
                // .rememberMeParameter()
                // 超时时间
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailService);

            // 退出
        http.logout()
                // 自定义退出连接
                .logoutUrl("/user/logout")
                // 退出成功后跳转的页面
                .logoutSuccessUrl("/login.html");

        // 关闭csrf跨站请求伪造,否则无法使用自定义登录逻辑
        http.csrf().disable();
    }

}
