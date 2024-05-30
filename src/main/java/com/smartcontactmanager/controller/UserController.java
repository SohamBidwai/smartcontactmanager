package com.smartcontactmanager.controller;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addCommonData(Model m, Principal principal){
        //This method used for sending information of login user to dashboard page.
        //Principal it's an part of java.security class. With the help of this we can fetch User Login Name or Username or User ID
        String name = principal.getName();
        System.out.println("Name: "+name);
        //get the user using username(Email)
        User user = this.userRepository.getUserByName(name);   //fetch the login user details against username.
        m.addAttribute("user1", user); //pass user details to user attribute of Model.
        m.addAttribute("title","User Details");
        //System.out.println("User Details: " + user);
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        //addCommonData(model, principal);
        model.addAttribute("title","User Dashboard");
        return "normal_user/userDashboard";
    }

    //open add form Handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact Details");
        model.addAttribute("contact",new Contact());    //this attribute fetch on addContactForm page
        return "normal_user/addContactForm";
    }

    @PostMapping("/process-contact")
    public String addContactDetails(@Valid @ModelAttribute("contact") Contact contact, BindingResult result1, Principal principal, Model model, HttpSession session)
    {
        try {

            if(result1.hasErrors()){
                System.out.println("Errors: " + result1.toString());
                //model.addAttribute("user","user");
                return "normal_user/addContactForm";
            }

            //System.out.println("Data Contact:  " + contact);
            String name = principal.getName();
            //System.out.println("name: "+ name);
            User user1 = this.userRepository.getUserByName(name);
            //System.out.println("Data Contact:  " + user1);
            contact.setUser(user1);
            user1.getContacts().add(contact);
            this.userRepository.save(user1);

            return "normal_user/addContactForm";
        }catch (Exception e){
            System.out.println("Exception in process-contact: "+ e.getMessage());
            return "normal_user/addContactForm";
        }
    }

}
