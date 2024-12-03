package com.smartcontactmanager.config;

import com.smartcontactmanager.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class MyConfiguration{

    @Autowired
    private JWTAuthenticationFillter jwtAuthenticationFillter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/home","/signup","/token","/do_register","/css/**", "/js/**", "/img/**").permitAll()
                                //.requestMatchers("/signup").hasRole("admin") // A. Restrict access to /admin/** to ADMIN role
                                //.requestMatchers("/signup/**").hasRole("admin") // B. Above line and this line both are used to set User Role Based Access.
                                //A is restrict the URL mentioned in requestMatchers and B is URL pattern that matches any request that starts with mentioned in requestMatchers.
                                .anyRequest().authenticated()
                );
        httpSecurity.formLogin(formLogin -> formLogin
                                        .defaultSuccessUrl("/home", true) // This also working, but It's always redirect to the home page after login when we try to access restricted URL.
                                        //.successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) // Redirect to original URL that are I'm trying to access
                                        .permitAll()
                                        .loginPage("/signin")
                                        .loginProcessingUrl("/dologin")  //dologin on this url we pass our username and password for login action.
                                        .successHandler(new CustomAuthenticationSuccessHandler())
                                        //Above line, the class is decided after login on which page is going to Land.
                                        //.defaultSuccessUrl("/dashboard")
                                        .failureUrl("/signin")
                                        ); //Above line of code indicate that, we provide our login page insted of Spring Security login Page.
                                        // Configure form login using lambda

                //Following code authenticate token for each request
        httpSecurity.addFilterBefore(jwtAuthenticationFillter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.logout(logout -> logout
                    .permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public UserDetailsService userDetailsService(){

        UserDetails adminUser = User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin123@#"))
                .roles("admin")
                .build();


        UserDetails juniorUser = User
                .withUsername("anup")
                .password(passwordEncoder().encode("Sbids@9876"))
                .roles("junior")
                .build();

        //InMemoryUserDetailsManager inMemoryUserDetailsManager =new InMemoryUserDetailsManager(adminUser, normalUser);

        return new InMemoryUserDetailsManager(adminUser, juniorUser);
    }*/



}
