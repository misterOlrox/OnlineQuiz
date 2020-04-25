package com.olrox.quiz.config;

import com.olrox.quiz.entity.Role;
import com.olrox.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/static/**", "/webjars/**", "/images/**", "/js/**").permitAll()
                .antMatchers("/", "/intro/*", "/rating/**", "/api/content/**").permitAll()
                .antMatchers("/setup/**", "/result/solo/**", "/play/**").hasAuthority(Role.USER.name())
                .antMatchers("/api/game/solo/**", "/api/invite/**").hasAuthority(Role.USER.name())
                .antMatchers("/api/users/**").authenticated()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/add-theme").hasAnyAuthority(Role.ADMIN.name(), Role.MODERATOR.name())
                .antMatchers("/add-prototype", "/add-question").authenticated()
                .antMatchers("/sign-up").not().authenticated()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()

                .and()
                .rememberMe().alwaysRemember(true)
                .and()
                .logout()
                .permitAll()

                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
