package com.easetest.website.controller;

import com.easetest.website.model.User;
import com.easetest.website.model.UserRegistrationDTO;
import com.easetest.website.repository.UserRepository;
import com.easetest.website.service.QuestionService;
import com.easetest.website.service.RecruiterService;
import com.easetest.website.service.TestService;
import com.easetest.website.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RecruiterService recruiterService;
    private final TestService testService;
    private final QuestionService questionService;
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/")
    public String index(Model model) {

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("current_user", username);
        System.out.println(principal);

        return "mainpage/index";
    }

    @GetMapping("/user")
    public String user() {
        return "mainpage/user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "mainpage/admin";
    }

    @GetMapping("/login")
    public String login() {
        return "mainpage/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "mainpage/index";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    //TODO autologin after registration
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserRegistrationDTO());
        return "mainpage/register_form";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute(value = "userDTO") UserRegistrationDTO userDTO, BindingResult result, Model model) {
        if (userDTO.getUserName() != null && usernameTaken(userDTO.getUserName())) {
            result.rejectValue("userName", "error.userDTO", "Username already exists.");
        }
        if (userDTO.getEmail() != null && emailTaken(userDTO.getEmail())) {
            result.rejectValue("email", "error.userDTO", "Email taken.");
        }
        if (result.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            System.out.println("User form error: " + userDTO);
            System.out.println(result);
            System.out.println("\n" + result);

            return "mainpage/register_form";
        }


        User user = userDTO.createUser();
        userService.save(user);
        return "mainpage/index";
    }

    private boolean usernameTaken(String username) {
        return userService.userExist(username);
    }

    // TODO create SQL query to check if email exist
    private boolean emailTaken(String email) {
        Set<User> users = userService.getAll();
        return users.stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
