package cn.gaoyuexiang.spring.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("test")
        .password("{noop}hello")
        .authorities("user")
        .and()
        .withUser("test2")
        .password("{noop}hello")
        .authorities("user")
        .and()
        .withUser("admin")
        .password("{noop}admin")
        .authorities("admin");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .mvcMatchers("hello")
        .hasAuthority("user")
        .anyRequest()
        .permitAll();
  }
}
