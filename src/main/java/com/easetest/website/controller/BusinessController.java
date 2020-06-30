package com.easetest.website.controller;

import com.easetest.website.model.*;
import com.easetest.website.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/business")
public class BusinessController {

    private final UserService userService;
    private final RecruiterService recruiterService;
    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    //TODO if recruiter is not created goto creation form else redirect to requested path
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        Recruiter recruiter = getRecruiter(authentication);
        if (recruiter != null) {
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
        //model.addAttribute("question", test.getQuestions().get(1));
        return "business/test_form";
    }

    @PostMapping("/editTest")
    public String editTest(Model model, @RequestParam("test_id") int id) {
        Test test = testService.getById(id);
        model.addAttribute("test", test);
        //model.addAttribute("question", test.getQuestions());
        return "business/test_form";
    }

    @PostMapping("/deleteTest")
    public String deleteTest(Model model, @RequestParam("test_id") int id, Authentication authentication) {
        testService.deleteById(id);
        List<Test> tests = getRecruiter(authentication).getTestList();
        model.addAttribute("test_list", tests);
        return "business/test_list";
    }

    //TODO Fix scenario where entering link straight to test creation throw error page after logging in (make it redirect to index page)
    //TODO Add testFormDTO with proper validation to avoid using Test object for sending only part of objects fields
    // (sending entire object would be also bad) use test_id to retrieve test and edit it
    //TODO Consider merge test and question form?
    @PostMapping("/saveTest")
    public String saveTest(@Valid @ModelAttribute(value = "test") Test test, BindingResult result, Model model, Authentication authentication) {
        Test realTest = testService.getById(test.getId());
        realTest.setTestName(test.getTestName());
        realTest.setNumberOfQuestions(test.getNumberOfQuestions());
        realTest.setMultipleAnswers(test.isMultipleAnswers());
        realTest.setStartTime(test.getStartTime());
        realTest.setTestTime(test.getTestTime());

        User user = getCurrentUser(authentication);
        Recruiter recruiter = user.getRecruiter();
        //TODO Create SQL query for test name validation
        for (Test t : recruiter.getTestList()) {
            if (t.getTestName().equals(test.getTestName()) && t.getId() != test.getId()) {
                result.rejectValue("testName", "error.test", "Test with that name already exists.");
            }
        }

        if (result.hasErrors()) {
            System.out.println("Save test errors: " + result.getAllErrors());
            model.addAttribute("test", test);
            return "business/test_form";
        }
        recruiter.addTest(realTest);
        recruiterService.save(recruiter);
        model.addAttribute("recruiter", recruiter);
        return "business/index";
    }

    //TODO if question body empty?
    @PostMapping("/editQuestion")
    public String editQuestion(Model model, @RequestParam("test_id") int id, @RequestParam(value = "question_id", required = false) Integer question_id) {
        Test test = testService.getById(id);
        List<Question> questions = test.getQuestions();
        System.out.println(questions);
        int realNumberOfQuestion = questions.size(); // mozliwe ze nie potrzebne
        test.setMultipleAnswers(false);
        Question q = question_id == null ? null : questionService.getById(question_id);
        if(q == null) {
            if(realNumberOfQuestion < 1) {
                q = new Question();
                q.setQuestionNumber(1);
                test.setQuestion(q);
                questionService.save(q);
            } else if(questions.get(realNumberOfQuestion - 1).getQuestionBody().isEmpty()){
                q = questions.get(realNumberOfQuestion - 1);
            } else {
                q = new Question();
                q.setQuestionNumber(realNumberOfQuestion + 1);
                test.setQuestion(q);
                questionService.save(q);
            }
        }

        model.addAttribute("test", test);
        model.addAttribute("question", q);
        System.out.println(q);

        return "business/question_form";
    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(Model model, @ModelAttribute("question") Question question, @RequestParam("test_id") int id) {
        Test test = testService.getById(id);
        for(Answer a : question.getAnswers()) {
            question.setAnswer(a);
        }
        test.setQuestion(question);
        testService.save(test);
        model.addAttribute("test", test);
        model.addAttribute("question", question);
        return "business/question_form";
    }

    @GetMapping("/showTestList")
    public String showTestList(Model model, Authentication authentication) {
        List<Test> tests = getRecruiter(authentication).getTestList();
        model.addAttribute("test_list", tests);
        System.out.println(tests);
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
