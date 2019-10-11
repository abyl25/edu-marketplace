package com.seniorproject.educationplatform.config;

import com.seniorproject.educationplatform.security.JwtConfigurer;
import com.seniorproject.educationplatform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    private static final String ADMIN_ENDPOINT = "/api/admin/**";
    private static final String AUTH = "/api/auth";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, AUTH + "/login", AUTH + "/signup").permitAll()
            .antMatchers(HttpMethod.GET, AUTH + "/confirmAccount").permitAll()
            .antMatchers(HttpMethod.POST, AUTH + "/forgotPassword").permitAll()
            .antMatchers(HttpMethod.GET, AUTH + "/validatePasswordResetToken").permitAll()
            .antMatchers(HttpMethod.POST, AUTH + "/updatePassword").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
            .antMatchers(HttpMethod.GET,"/api/courses/**").permitAll()
            .antMatchers("/api/courses/**").hasAuthority("Instructor")
            .antMatchers("/api/cart/**").permitAll()

            .antMatchers(ADMIN_ENDPOINT).hasRole("Admin")
            .anyRequest().authenticated()
            .and()
            .apply(new JwtConfigurer(jwtTokenProvider));
    }

//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
