package com.fionera.test.configure;

import com.fionera.test.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration
        .WebSecurityConfigurerAdapter;

/**
 * SecurityConfiguration
 * Created by fionera on 2018/3/8 in spring_demo.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration
        extends WebSecurityConfigurerAdapter {

    private final UserMapper userMapper;

    @Autowired
    public SecurityConfiguration(UserMapper userMapper) {
        super();
        this.userMapper = userMapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("Enable AuthenticationManagerBuilder Configuration");
        auth.userDetailsService(userMapper::findByName);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Enable HttpSecurity Configuration");
        http.authorizeRequests().antMatchers("/").access("hasRole('ACCESS')").antMatchers("/**")
                .permitAll().and().formLogin();
    }
}