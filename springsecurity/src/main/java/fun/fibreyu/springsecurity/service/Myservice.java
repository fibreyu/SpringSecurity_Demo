package fun.fibreyu.springsecurity.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface Myservice {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
