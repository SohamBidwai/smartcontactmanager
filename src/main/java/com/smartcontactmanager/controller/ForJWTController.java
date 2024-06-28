package com.smartcontactmanager.controller;

import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.entities.JWTResponse;
import com.smartcontactmanager.helper.JWTHelper;
import com.smartcontactmanager.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForJWTController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;    //we create bean of AuthenticationManager class in MyConfiguration class

    @RequestMapping("/checking")
    public String checking(){
        String text = "Authorization";
        return text;
    }

    //JWT generation code
    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody User user) throws Exception {
        //To generate token we need to accept username and password of user so we used User
        //To authenticate username and password following code is used [before token generate]
        System.out.println("User Info: "+user);
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            //UsernamePasswordAuthenticationToken this is an inbuild class.

        }catch (UsernameNotFoundException e){
            //e.printStackTrace();
            return new ResponseEntity<>("User Not Found", HttpStatus.UNAUTHORIZED);
        }catch (BadCredentialsException ee){
            //ee.printStackTrace();
            return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
        }

        //after successfully authenticate following code execute
        //fetch user details
        //UserDetails is an in-build Interface
        UserDetails userDetails =  this.customUserDetailsService.loadUserByUsername(user.getEmail());

        //above userDetails we pass to generateToken method
        //userDetails contains username and password [Login credentials]
        String usertoken = this.jwtHelper.generateToken(userDetails);
        System.out.println("user Token: " + usertoken);

        //we need to pass token in JSON form so we used JWTResponse class
        //{"token":usertoken}

        return ResponseEntity.ok(new JWTResponse(usertoken));
    }

}
