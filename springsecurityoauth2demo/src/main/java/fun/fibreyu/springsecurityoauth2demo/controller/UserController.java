package fun.fibreyu.springsecurityoauth2demo.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author fibreyu
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

    /**
     * 获取当前用户
     *
     * 可以通过该请求，解析jwt的token
     * @param authentication
     * @param request
     * @return
     */
    @RequestMapping("/getCurrentJwtUser")
    public Object getCurrentJwtUser(Authentication authentication, HttpServletRequest request) {
        // 通过请求头获取Authorization
        String header = request.getHeader("Authorization");
        // 根据header截取jwt对应的令牌
        // 为什么截取7位，因为放在Authorization里面的value是以”Bearer“开头中间加空格
        String token = header.substring(header.indexOf("bearer") + 7);
        // 放进密钥，设置utf-8
        return Jwts.parser()
                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
