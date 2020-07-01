package com.easetest.website.controller;

import com.easetest.website.model.*;
import com.easetest.website.repository.UserRepository;
import com.easetest.website.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RecruiterService recruiterService;
    private final TestService testService;
    private final QuestionService questionService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CandidateService candidateService;


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

    @GetMapping("/takeTheTest")
    public String takeTheTest(Model model) {
        model.addAttribute("personalKey", "");
        model.addAttribute("keyAlert", "");
        return "candidate/personal_key_form";
    }

    @PostMapping("/checkTestKey")
    public String checkTestKey(Model model, @RequestParam("personalKey") String personalKey, @RequestParam("keyAlert") String keyAlert) {
        Candidate candidate = candidateService.getByPersonalKey(personalKey);
        if (candidate == null) {
            model.addAttribute("keyAlert", "Wrong key.");
            model.addAttribute("personalKey", personalKey);
            return "candidate/personal_key_form";
        } else if (candidate.isCompleted()) {
            model.addAttribute("keyAlert", "Key already used.");
            model.addAttribute("personalKey", personalKey);
            return "candidate/personal_key_form";
        } else {
            CandidateTestFormDTO candidateTestFormDTO =
                    new CandidateTestFormDTO(candidate);
            candidateTestFormDTO.setTestId(candidate.getTest().getId());
            model.addAttribute("candidateTestFormDTO", candidateTestFormDTO);
            return "candidate/candidate_data_form";
        }
    }

    @PostMapping("/startTest")
    public String startTest(Model model, @Valid @ModelAttribute("candidateTestFormDTO") CandidateTestFormDTO candidateTestFormDTO, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("candidateTestFormDTO", candidateTestFormDTO);
            return "candidate/candidate_data_form";
        }
        Test test = testService.getById(candidateTestFormDTO.getTestId());

        Candidate candidate = test.getCandidate(candidateTestFormDTO.getPersonalKey());
        candidate.setName(candidateTestFormDTO.getName());
        candidate.setSurname(candidateTestFormDTO.getSurname());
        candidate.setEmail(candidateTestFormDTO.getEmail());
        candidate.setStartDate(LocalDateTime.now());
        testService.save(test);
        CandidateTestAnswersDTO candidateTestAnswersDTO = new CandidateTestAnswersDTO();
        candidateTestAnswersDTO.setQuestion(test.getQuestions().get(0));
        candidateTestAnswersDTO.setCandidateId(candidate.getId());
        candidateTestAnswersDTO.setTestId(test.getId());
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("test", test);
        model.addAttribute("candidateTestAnswersDTO", candidateTestAnswersDTO);
        model.addAttribute("testStart", candidate.getStartDate());

        return "candidate/candidate_test_form"; //DO ZROBIENIA FORMULARZ TESTOWY
    }

    @PostMapping("setCandidateAnswer")
    public String setCandidateAnswer(Model model, @ModelAttribute("CandidateTestAnswersDTO") CandidateTestAnswersDTO candidateTestAnswersDTO) {
        Test test = testService.getById(candidateTestAnswersDTO.getTestId());
        Candidate candidate = candidateService.getById(candidateTestAnswersDTO.getCandidateId());
        int questionId = candidateTestAnswersDTO.getQuestionId();
        candidateTestAnswersDTO.setQuestion(questionService.getById(questionId));
        candidateTestAnswersDTO.updateCandidateAnswers(candidate);
        candidateService.save(candidate);
        int nextQuestionIndex = candidateTestAnswersDTO.getQuestion().getQuestionNumber();

        Question question = test.getQuestions().size() > nextQuestionIndex ?
                test.getQuestions().get(nextQuestionIndex) :
                test.getQuestions().get(0);
        candidateTestAnswersDTO.setCandidateAnswersForQuestion(question, candidate);
        candidateTestAnswersDTO.setQuestion(question);

        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("test", test);
        model.addAttribute("candidateTestAnswersDTO", candidateTestAnswersDTO);
        model.addAttribute("testStart", candidate.getStartDate());
        return "candidate/candidate_test_form";
    }

    @PostMapping("/submitTest")
    public String submitTest(Model model, @ModelAttribute("CandidateTestAnswersDTO") CandidateTestAnswersDTO candidateTestAnswersDTO) {
        Candidate candidate = candidateService.getById(candidateTestAnswersDTO.getCandidateId());
        Test test = testService.getById(candidateTestAnswersDTO.getTestId());
        candidate.setCompleted(true);
        candidate.setCompletedDate(LocalDateTime.now());
        candidateService.save(candidate);

        List<String> results = candidate.getResults();
        List<Question> questions = test.getQuestions();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        List<String> correctAnswersList = new ArrayList<>();
        for(Question q : questions) {
            sb.append(q.getQuestionNumber());
            if(q.getAnswers().get(0).isCorrect()) {
                sb.append("a");
            }
            if(q.getAnswers().get(1).isCorrect()) {
                sb.append("b");
            }
            if(q.getAnswers().get(2).isCorrect()) {
                sb.append("c");
            }
            if(q.getAnswers().get(3).isCorrect()) {
                sb.append("d");
            }
            correctAnswersList.add(q.getQuestionNumber() - 1, sb.toString());
            sb.setLength(0);
        }
        String joinedResults = results.stream()
                .collect(Collectors.joining("\n"));
        String correctAnswers = correctAnswersList.stream()
                .collect(Collectors.joining("\n"));

        model.addAttribute("joinedResults", joinedResults);
        model.addAttribute("correctAnswers", correctAnswers);

        return "candidate/simple_summary";
    }
}
