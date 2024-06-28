package com.smartcontactmanager.controller;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required=true)
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/home")
    public String home(Model model){

        model.addAttribute("title","Welcome to Smart Contact Manager..");
        return "home";
    }

    @RequestMapping("/signin")
    public String loginPage(Model model){
        model.addAttribute("title","Login to Contact Manager");

        return "login";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About Smart Contact Manager..");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model, HttpSession session){
        model.addAttribute("title","Sign Up Page");
        model.addAttribute("user",new User());  //this attribute hold form fields values during submitting

        session.removeAttribute("message");
        return "signup";
    }

    //following handler for registration
    @RequestMapping(value="/do_register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "terms_condition",defaultValue = "false")
    boolean terms_condition, @RequestParam("file") MultipartFile file, Model model, HttpSession session){


        try {
            if(result1.hasErrors()){
                System.out.println("Errors: " + result1.toString());
                //model.addAttribute("user","user");
                return "signup";
            }

            if(!terms_condition){
                session.setAttribute("message",new Message("Please accept terms and conditions.","danger"));
                return "signup";
            }

            if(!user.getEmail().equals("") && user.getId()==0){
                User user1 = this.userRepository.getUserByName(user.getEmail());
                if(user1!=null) {
                    if(!user1.getEmail().equals("")) {
                        session.setAttribute("message", new Message("Email already exist in system.", "danger"));
                        user.setEmail("");
                        return "signup";
                    }
                }
            }

            if(!file.isEmpty()){
                File uploadingFile =new ClassPathResource("static/img").getFile(); //For upload file path
                Path path =  Paths.get(uploadingFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);   //if same name file have so replace existing with new one
                user.setImageurl(file.getOriginalFilename()); //set image name
            }else{
                user.setImageurl("user.png");
            }

            if(user.getId()==0)     //Fresh SignUp User
            {
                user.setRole("Normal User");
                user.setEnabled(true);
                //user.setImgurl("aa.png");
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User result = this.userRepository.save(user);
                model.addAttribute("user", new User());
                session.setAttribute("message",new Message("alert-success","Successfully Registred..!"));

                return "home";
            }else   //Update User Details
            {
                user.setRole("Normal User");
                user.setEnabled(true);
                user.setId(user.getId());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User result = this.userRepository.save(user);
                session.setAttribute("message",new Message("alert-success","Successfully Registred..!"));

                return "redirect:/user/user-profile";
            }

        }catch (Exception e){
            e.printStackTrace();
            //model.addAttribute("user","user");
            session.setAttribute("message",new Message("alert-danger","Something went Wrong"));

            return "signup";
        }
    }
}
