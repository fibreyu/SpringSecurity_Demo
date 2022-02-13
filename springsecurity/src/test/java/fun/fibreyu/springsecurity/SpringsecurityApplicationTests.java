package fun.fibreyu.springsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SpringsecurityApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder pw = new BCryptPasswordEncoder();
        // 加密
        String encode = pw.encode("123");
        System.out.println(encode);
        // 比较密码
        boolean matches = pw.matches("123", encode);
        System.out.println(matches);
    }
}
