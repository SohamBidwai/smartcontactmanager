package com.smartcontactmanager.controller;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/home")
    public String home(Model model){

        model.addAttribute("title","Welcome to Smart Contact Manager..");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About Smart Contact Manager..");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Sign Up Page");
        model.addAttribute("user",new User());  //this attribute hold form fields values during submitting
        return "signup";
    }

    //following handler for registration
    @RequestMapping(value="/do_register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "terms_condition",defaultValue = "false")
    boolean terms_condition, Model model, HttpSession session){

        try {

            if(!terms_condition){

                return "signup";
            }

            if(result1.hasErrors()){
                System.out.println("Errors: " + result1.toString());
                //model.addAttribute("user","user");
                return "n";
            }

            user.setRole("Normal User");
            user.setEnabled(true);
            //user.setImgurl("aa.png");

            User result = this.userRepository.save(user);
            model.addAttribute("user",new User());

            session.setAttribute("message",new Message("alert-success","Successfully Registred..!"));

            return "home";
        }catch (Exception e){
            e.printStackTrace();
            //model.addAttribute("user","user");
            session.setAttribute("message",new Message("alert-danger","Something went Wrong"));

            return "signup";
        }
    }
}
