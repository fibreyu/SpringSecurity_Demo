package fun.fibreyu.springsecurity.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fibreyu
 * @since 1.0.0
 */
@Controller
public class LoginController {

    /**
     * 登录
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "redirect:main.html";
    }

    /**
     * 在SecurityConfig中配置了successForwardUrl方法设置跳转页面
     * 上面的login接口就无效了，相当于login接口被Spring Security接管了
     * 不用自定义login了
     * @return
     */
    // @Secured("ROLE_abc")
    @PreAuthorize("hasRole('abc')")
    @RequestMapping("/toMain")
    public String toMain() {
        return "redirect:main.html";
    }

    /**
     *
     * 登录失败跳转页面
     * @return
     */
    @RequestMapping("/toError")
    public String toError() {
        return "redirect:error.html";
    }

    /**
     * 页面跳转
     * @return
     */
    /*@GetMapping("/demo")
    @ResponseBody
    public String demo() {
        return "demo";
    }*/

    /**
     * 跳转到模板
     * @return
     */
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

    /**
     * 跳转到模板login
     * @return
     */
    @GetMapping("/showLogin")
    public String showLogin() {
        return "login";
    }
}
