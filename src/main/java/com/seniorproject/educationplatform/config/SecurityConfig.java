package com.seniorproject.educationplatform.config;

import com.seniorproject.educationplatform.filters.AddResponseHeaderFilter;
import com.seniorproject.educationplatform.security.JwtConfigurer;
import com.seniorproject.educationplatform.security.JwtTokenFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private AddResponseHeaderFilter responseHeaderFilter;

    private static final String ADMIN_ENDPOINT = "/api/admin/**";
    private static final String AUTH = "/api/auth";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
            AddResponseHeaderFilter responseHeaderFilter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.responseHeaderFilter = responseHeaderFilter;
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
            .antMatchers("/").permitAll()
            .antMatchers("/rest").permitAll()

            // Auth, User and Role routes
            .antMatchers(HttpMethod.GET, "/api/users/**").permitAll()
            .antMatchers("/api/users/**").hasAuthority("Admin")
            .antMatchers(HttpMethod.POST, AUTH + "/login", AUTH + "/signup").permitAll()
            .antMatchers(HttpMethod.GET, AUTH + "/confirmAccount").permitAll()
            .antMatchers(HttpMethod.POST, AUTH + "/forgotPassword").permitAll()
            .antMatchers(HttpMethod.GET, AUTH + "/validatePasswordResetToken").permitAll()
            .antMatchers(HttpMethod.POST, AUTH + "/updatePassword").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
            .antMatchers("/api/roles/**").permitAll()
            .antMatchers("/api/role/**").permitAll()

            // Course routes
            .antMatchers(HttpMethod.GET, "/api/user/*/courses/**").permitAll()
//            .antMatchers(HttpMethod.GET,"/api/courses/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/courses/**").hasAuthority("Instructor")
            .antMatchers("/api/courses/**").permitAll()
//            .antMatchers(HttpMethod.POST,"/api/courses/target").hasAuthority("Instructor")
            .antMatchers("/api/courses/target").permitAll()

            // Cart routes
            .antMatchers(HttpMethod.GET,"/api/instructor/*/courses/*").permitAll()
            .antMatchers("/api/user/*/cart/**").permitAll()
            .antMatchers("/api/cart/**").permitAll()
            .antMatchers("/api/payment/**").permitAll()

            // Category and Topic routes
            .antMatchers("/api/category/**").permitAll()
            .antMatchers("/api/categories/**").permitAll()
            .antMatchers("/api/subcategories/**").permitAll()
            .antMatchers("/api/topics/**").permitAll()
            .antMatchers("/api/topic/**").permitAll()

            // Admin routes
            .antMatchers(ADMIN_ENDPOINT).hasRole("Admin")

            .anyRequest().authenticated()
            .and()
            .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Cache-Control", "Origin", "Accept", "Accept-Language",
            "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
