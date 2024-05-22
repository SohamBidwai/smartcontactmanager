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
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                                        //.defaultSuccessUrl("/home", true) // This also working, but It's always redirect to the home page after login when we try to access restricted URL.
                                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) // Redirect to original URL that are I'm trying to access
                                        .permitAll()); // Configure form login using lambda
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails adminUser = User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin123@#"))
                .roles("admin")
                .build();


        UserDetails normalUser = User
                .withUsername("soham")
                .password(passwordEncoder().encode("Sbids@9876"))
                .roles("junior")
                .build();

        //InMemoryUserDetailsManager inMemoryUserDetailsManager =new InMemoryUserDetailsManager(adminUser, normalUser);

        return new InMemoryUserDetailsManager(adminUser, normalUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
