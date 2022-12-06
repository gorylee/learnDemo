package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author GoryLee
 * @Date 2022/12/5 20:24
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置放行路径，不用验证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf功能
                .csrf().disable()
                //不通过session获取securityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许登录接口匿名访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/addUser").anonymous()
                // 除了上面的路径，其他都需要鉴权访问
                .anyRequest().authenticated();
    }

    /**
     * 注入AuthenticationManager到spring容器中，用于用户登录时调用方法验证
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    public static void main(String[] args) {
//
//        //$2a$10$fl.1X4NRsNcUjaTpfzb8Ve9JZR6Rgr1tmQ1HmFA720ye1VQl6T6Qm   --1234
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encode = passwordEncoder.encode("1234");
//        System.out.println(encode);
//
//
//    }
}
