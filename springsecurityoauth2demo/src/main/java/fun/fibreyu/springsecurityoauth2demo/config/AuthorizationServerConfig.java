package fun.fibreyu.springsecurityoauth2demo.config;

import fun.fibreyu.springsecurityoauth2demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.support.collections.RedisStore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * authorization server config
 *
 * @author fibreyu
 * @since 1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("redisTokenStore")
    private TokenStore tokenStore;

    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore jwtTokenStore;

    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;



    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // ??????JWT????????????
        TokenEnhancerChain chain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        chain.setTokenEnhancers(delegates);
        /*endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                .tokenStore(tokenStore);*/
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService)
                // accessToken??????jwttoken
                .tokenStore(jwtTokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(chain);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // ?????????id
                .withClient("client")
                // ??????
                .secret(passwordEncoder.encode("1112233"))
                // ????????????token????????????
                .accessTokenValiditySeconds(3600)
                // ????????????token????????????
                .refreshTokenValiditySeconds(864000)
                // ??????redirect_uri??????????????????????????????
                // .redirectUris("http://www.baidu.com")
                // ????????????????????????????????????
                .redirectUris("http://localhost:8081/login")
                // ????????????
                .autoApprove(true)
                // ????????????
                .scopes("all")
                // ????????????
                // authorization_code ???????????????
                // password ????????????
                // refresh_token ????????????
                .authorizedGrantTypes("authorization_code", "password", "refresh_token");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // ?????????????????????????????????????????????????????????????????????
        security.tokenKeyAccess("isAuthenticated()");
    }
}
