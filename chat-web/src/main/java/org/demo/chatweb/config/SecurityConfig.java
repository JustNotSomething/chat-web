package org.demo.chatweb.config;


import org.demo.chatweb.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService, JWTFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    // Authentication Configure


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Configure

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers( "/api/login","/api/registration", "/users", "/api/profile", "/api/chats", "/api/chats/avatars", "/api/getUsers", "/api/saveUser", "/api/deleteUser", "/websocket-endpoint/**", "/api/chats/loadHistory", "/api/chats/getLastMessages", "/api/deleteProfile", "/api/updateUser", "/api/sections", "/api/sections/getOther" , "/createMany",  "/api/sections/create",  "/api/sections/update", "/api/hideProfile", "/api/revealProfile" , "/api/getProfileVisibility", "/api/getAllUsers", "/api/deleteUserAccount", "/api/changeUserRole", "/api/file/**", "/api/getUsers/avatars", "/api/sections/getOther/avatars", "/api/getAllUsers/avatars", "/api/status/**").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





}
