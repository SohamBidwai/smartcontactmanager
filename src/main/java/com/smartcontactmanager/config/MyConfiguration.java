package com.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class MyConfiguration{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/home","/do_register","/css/**", "/js/**", "/images/**").permitAll()
                                //.requestMatchers("/signup").hasRole("admin") // A. Restrict access to /admin/** to ADMIN role
                                //.requestMatchers("/signup/**").hasRole("admin") // B. Above line and this line both are used to set User Role Based Access.
                                //A is restrict the URL mentioned in requestMatchers and B is URL pattern that matches any request that starts with mentioned in requestMatchers.
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                                        .defaultSuccessUrl("/home", true) // This also working, but It's always redirect to the home page after login when we try to access restricted URL.
                                        //.successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) // Redirect to original URL that are I'm trying to access
                                        .permitAll()
                                        .loginPage("/signin")
                                        .loginProcessingUrl("/dologin")  //dologin on this url we pass our username and password for login action.
                                        .successHandler(new CustomAuthenticationSuccessHandler())
                                        //Above line, the class is decided after login on which page is going to Land.
                                        //.defaultSuccessUrl("/dashboard")
                                        .failureUrl("/signin")
                                        ) //We provide our logiin page insted of Spring Security login Page.
                                        // Configure form login using lambda
                .logout(logout -> logout
                    .permitAll()
                );
        return httpSecurity.build();
    }

    @Bean
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
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
