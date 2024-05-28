package com.smartcontactmanager.controller;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        //This method used for sending information of login user to dashboard page.
        //Principal it's an part of java.security class. With the help of this we can fetch User Login Name or Username or User ID

        String name = principal.getName();
        System.out.println("Name: "+name);
        //get the user using username(Email)
        User user = this.userRepository.getUserByEmail(name);   //fetch the login user details against username.
        model.addAttribute("user1", user); //pass user details to user attribute of Model.
        model.addAttribute("title","User Details");
        //System.out.println("User Details: " + user);

        return "normal_user/userDashboard";
    }

}
