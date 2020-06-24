package com.easetest.website.controller;

import com.easetest.website.model.Recruiter;
import com.easetest.website.model.Test;
import com.easetest.website.model.User;
import com.easetest.website.model.UserRegistrationDTO;
import com.easetest.website.service.RecruiterService;
import com.easetest.website.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/business")
public class BusinessController {

    private final UserService userService;
    private final RecruiterService recruiterService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        Recruiter recruiter = getRecruiter(authentication);
        if(recruiter != null) {
            model.addAttribute("recruiter", recruiter);
            return "business/index";
        } else {
            //TODO Separate this case part as different method (/register)
            recruiter = new Recruiter();
            recruiter.setEmail(getCurrentUser(authentication).getEmail());
            System.out.println(recruiter);
            model.addAttribute("recruiter", recruiter);
            return "business/register_form";
        }
    }

    @PostMapping("/saveRecruiter")
    public String saveUser(@Valid @ModelAttribute(value = "recruiter") Recruiter recruiter, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("recruiter", recruiter);
            return "business/register_form";
        }
        User user = getCurrentUser(authentication);
        user.setRecruiter(recruiter);
        userService.save(user);
        model.addAttribute("recruiter", recruiter);
        return "business/index";
    }

    @GetMapping("/createTest")
    public String createTest(Model model) {
        Test test = new Test();
        model.addAttribute("test", test);
        return "business/create_test_form";
    }

    //TODO Fix scenario where entering link straight to test creation throw error page after logging in (make it redirect to index page)
    @PostMapping("/saveTest")
    public String saveUser(@Valid @ModelAttribute(value = "test") Test test, BindingResult result, Model model, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Recruiter recruiter = user.getRecruiter();
        //TODO Create SQL query for test name validation
        for(Test t: recruiter.getTestList()) {
            if(t.getTestName().equals(test.getTestName())) {
                result.rejectValue("testName", "error.test", "Test with that name already exists.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("test", test);
            return "business/create_test_form";
        }

        recruiter.addTest(test);
        recruiterService.save(recruiter);
        model.addAttribute("recruiter", recruiter);
        return "business/index";
    }

    @GetMapping("/showTestList")
    public String showTestList(Model model) {
        //TODO implement viewing tests
        return "business/test_list";
    }
    private boolean isRecruiter(Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
           return userService.getByUsername(currentUserName).getRecruiter() != null;
        }
        return false;
    }

    private Recruiter getRecruiter(Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userService.getByUsername(currentUserName).getRecruiter();
        }
        return null;
    }

    private User getCurrentUser(Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userService.getByUsername(currentUserName);
        }
        return null;
    }
}
