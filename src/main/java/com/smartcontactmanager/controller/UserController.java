package com.smartcontactmanager.controller;

import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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
    public String dashboard(Model model, Principal principal, HttpSession session){
        //addCommonData(model, principal);
        model.addAttribute("title","User Dashboard");

        session.removeAttribute("message");
        return "normal_user/userDashboard";
    }

    //open add form Handler(jst open the page of add contact form)
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Contact Details");
        model.addAttribute("contact",new Contact());    //this attribute fetch on addContactForm page
        return "normal_user/addContactForm";
    }

    //following action used to add the contact details fill by the user
    @PostMapping("/process-contact")
    public String addContactDetails(@Valid @ModelAttribute("contact") Contact contact, BindingResult result1,
                                    @RequestParam("profileImage") MultipartFile file, Principal principal, Model model, HttpSession session)
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

            //Uploading file
            if(file.isEmpty()){
                //file is empty
                //return "normal_user/addContactForm";
            }else{
                //upload file and concat the name

                System.out.println("Check File type: "+ file.getContentType());

                String fileType = file.getOriginalFilename().toString();
                /*if (fileType.equals("image/png") || fileType.equals("image/jpeg") || fileType.equals("image/jpg")) {*/
                    File uploadingFile =new ClassPathResource("static/img").getFile();
                    //path for uploading file
                    Path path =  Paths.get(uploadingFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                    //Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING)
                    //First param is source, second param is target to store, third param is copy option.
                    //If you want to see image in your workspace then goto build folder and then find it.
                    //Build folder means where project created it's own build folder called build folder name should be anything.
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
               /* }else{
                    File uploadingFile =new ClassPathResource("static/docs").getFile();
                    Path path =  Paths.get(uploadingFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }*/
                System.out.println("Image is uploaded is successfully..!");
            }

            contact.setUser(user1);
            user1.getContacts().add(contact);
            this.userRepository.save(user1);
            //on above line data(contact) is stored successfully.
            //following code for to show message for successfully stored contact details
            session.setAttribute("message",new Message("Contact added successfully.", "success"));

            return "normal_user/addContactForm";
        }catch (Exception e){
            System.out.println("Exception in process-contact: "+ e.getMessage());
            //in case have some error so following  code passs message accordinglly
            session.setAttribute("message",new Message("Can't save the details try again.", "danger"));
        }
        return "normal_user/addContactForm";
    }


    //show contact list of a user
    //following is an List type controller, in this controller we
    //pegination is also handle from backend. (per page 10 record)
    @GetMapping("/getcontact/{page}")
    public String contactList(@PathVariable("page") Integer page,Model model, Principal principal){
        model.addAttribute("title","User Contact List");
            //first way
            String userName = principal.getName();
            User user = this.userRepository.getUserByName(userName);
            //user.getContacts();

            //Pegination
            //In below code page is an number of the particular page and 5 is an no of records show on per page.
            Pageable pageable = PageRequest.of(page, 5);

            //second way
            Page<Contact> userContactList = this.contactRepository.findAllById(user.getId(), pageable);
            model.addAttribute("contactList", userContactList);
            model.addAttribute("currentPage", page);    //It's a part if pegination
            model.addAttribute("totalPages", userContactList.getTotalPages()); //It's a part if pegination

        return "normal_user/viewContactsList";

    }

}
