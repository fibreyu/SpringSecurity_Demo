package fun.fibreyu.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author fibreyu
 * @since 1.0.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("执行自定义登录逻辑!");

        // 1、根据用户名去数据库查询，如果不存在直接抛异常 UsernameNotFoundException
        if(!"admin".equals(username)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 2、比较密码 用户注册时存的密码是passwordencoder加密后的密文
        // 如果匹配成功返回UserDetails
        String password = passwordEncoder.encode("123");
        // passwordEncoder.matches("123", "password from database");

        return new User(username, password, AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin,normal,ROLE_abc"));

        /*return new User(username, password, AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin,ROLE_abc,/insert,/delete"));*/
    }
}
