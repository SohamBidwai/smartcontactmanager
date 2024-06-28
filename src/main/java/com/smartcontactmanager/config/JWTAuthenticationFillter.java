package com.smartcontactmanager.config;

import com.smartcontactmanager.helper.JWTHelper;
import com.smartcontactmanager.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFillter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get jwt
        //check start with Bearer or Not
        //validate token

       //IMP IMP IMP IMP IMP Note
       //After successfully authenticate token from following code we need to configure this JWTAuthenticationFilter with our MyConfiguration class.
       //MyConfiguration means our SpringSecurity configuration file.

        String userToken = request.getHeader("Authorization");  //fetch user token
        String username=null;
        String jwttoken=null;
        System.out.println("Check: "+ userToken);

        if(userToken!=null && userToken.startsWith("Bearer "))
        {
            jwttoken = userToken.substring(7);  //fetch exact token
            try {
                username = this.jwtHelper.extractUsername(jwttoken);    //extreact username from token and Pass To CustomUserDetailsService

            }catch (Exception e){
                e.printStackTrace();
            }

            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            //On above line we fetch username and password

            //following code for security purpose [Authenticate token]
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()!=null){
                    //Here we validate token
                    //First validate username and password
                UsernamePasswordAuthenticationToken userAuthtokenInfo = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                userAuthtokenInfo.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //On below line we set our user details to security if below line accept so means all is ok
                SecurityContextHolder.getContext().setAuthentication(userAuthtokenInfo);

                //Above three line of code is used for authenticate token

            }

        }

        filterChain.doFilter(request, response);

    }
}
