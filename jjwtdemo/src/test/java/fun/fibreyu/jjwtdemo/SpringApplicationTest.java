package fun.fibreyu.jjwtdemo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fibreyu
 * @since 1.0.0
 */
@SpringBootTest
public class SpringApplicationTest {

    /**
     * 生成jwt
     */
    @Test
    public void testJwt() {
        // 创建一个JwtBuilder对象
        JwtBuilder jwtBuilder = Jwts.builder()
                // 唯一ID{"id": 888}
                .setId("888")
                // 接受的用户 {"sub": "Rose"}
                .setSubject("Rose")
                // 签发时间 {"iat": "..."}
                .setIssuedAt(new Date())
                // 签名算法，及密钥
                .signWith(SignatureAlgorithm.HS256, "xxxx");
        // 获取jwt的token
        String token = jwtBuilder.compact();
        System.out.println(token);

        // 三部分的base64解密
        System.out.println("----------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 无法解密
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }

    /**
     * 解析token
     */
    @Test
    public void testParseToken() {
        // token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjQ0NzYwNTUzfQ.-xkfxCWpUEDPsd1hx0MU-ON9R9IiQIuz-EPvAzNK1H0";
        // 解析token获取负载中的声明对象
        Claims claims = Jwts.parser()
                .setSigningKey("xxxx")
                .parseClaimsJws(token)
                .getBody();

        // 打印声明的属性
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("issuedAt:" + claims.getIssuedAt());
    }

    /**
     * 生成过期token
     */
    @Test
    public void testCreateTokenHasExp() {
        // 获取当前的时间
        long now = System.currentTimeMillis();
        // 失效的具体时间
        long exp = now + 60 * 1000;

        // 创建JwtBuild对象
        JwtBuilder jwtBuilder = Jwts.builder()
                // 唯一ID{"id": 888}
                .setId("888")
                // 接受的用户 {"sub": "Rose"}
                .setSubject("Rose")
                // 签发时间 {"iat": "..."}
                .setIssuedAt(new Date())
                // 签名算法，及密钥
                .signWith(SignatureAlgorithm.HS256, "xxxx")
                // 设置失效时间
                .setExpiration(new Date(exp));

        // 获取jwt的token
        String token = jwtBuilder.compact();
        System.out.println(token);
        // 三部分的base64解密
        System.out.println("----------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 无法解密
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }

    /**
     * 解析过期token
     */
    @Test
    public void testParseTokenHasExp() {
        // token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjQ0NzY1MTgyLCJleHAiOjE2NDQ3NjUyNDJ9.8XJM8tUpF8FjKdVKAJzsI9hmTUzlhdHzoJsRxdOb-7c";
        // 解析token获取负载中的声明对象
        Claims claims = Jwts.parser()
                .setSigningKey("xxxx")
                .parseClaimsJws(token)
                .getBody();

        // 打印声明的属性
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("issuedAt:" + claims.getIssuedAt());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("签发时间: " + simpleDateFormat.format(claims.getIssuedAt()));
        System.out.println("过期时间: " + simpleDateFormat.format(claims.getExpiration()));
        System.out.println("现在时间: " + simpleDateFormat.format(new Date()));
    }

    /**
     * 生成token自定义声明
     */
    @Test
    public void testJwtHasEnhancer() {

        // 创建JwtBuild对象
        JwtBuilder jwtBuilder = Jwts.builder()
                // 唯一ID{"id": 888}
                .setId("888")
                // 接受的用户 {"sub": "Rose"}
                .setSubject("Rose")
                // 签发时间 {"iat": "..."}
                .setIssuedAt(new Date())
                // 签名算法，及密钥
                .signWith(SignatureAlgorithm.HS256, "xxxx")
                .claim("name", "Jack")
                .claim("logo", "xxx.jpg");
                //.addClaims(map);

        // 获取jwt的token
        String token = jwtBuilder.compact();
        System.out.println(token);
        // 三部分的base64解密
        System.out.println("----------------------------------");
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 无法解密
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));
    }

    /**
     * 解析token自定义声明
     */
    @Test
    public void testParseTokenHasEnhancer() {
        // token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjQ0NzY1ODc2LCJuYW1lIjoiSmFjayIsImxvZ28iOiJ4eHguanBnIn0.RXdqYibGYHO2QQZfEQi3gYrU8NuAy4P1jxOO9-v1FLc";
        // 解析token获取负载中的声明对象
        Claims claims = Jwts.parser()
                .setSigningKey("xxxx")
                .parseClaimsJws(token)
                .getBody();

        // 打印声明的属性
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("issuedAt:" + claims.getIssuedAt());
        System.out.println("name: " + claims.get("name"));
        System.out.println("logo: " + claims.get("logo"));

    }
}
