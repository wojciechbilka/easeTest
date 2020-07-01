package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @EqualsAndHashCode.Exclude
    @NotEmpty(message = "Field cannot be empty.")
    private String testName = "";
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Field cannot be empty.")
    @Future(message = "Field must contain a future date.")
    private LocalDateTime startTime;
    @EqualsAndHashCode.Exclude
    @NotNull(message = "Field cannot be empty")
    @Min(value = 5, message = "Minimum value is 5.")
    @Max(value = 120, message = "Maximum value is 120.")
    private Integer testTime; //minutes
    @EqualsAndHashCode.Exclude
    private boolean multipleAnswers;
    //possibility to user part of all questions in test
    @EqualsAndHashCode.Exclude
    @Min(value = 0, message = "Minimum value is 0.")
    @Max(value = 100, message = "Maximum value is 100.")
    @NotNull(message = "Field cannot be empty")
    private Integer numberOfQuestions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test", orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test", orphanRemoval = true)
    private List<Candidate> candidates = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter owner;

    public void setQuestion(Question question) {
        question.setTest(this);
        System.out.println("Questions size: " + questions.size());
        System.out.println("Question number:" + question.getQuestionNumber());
        if (questions.size() < question.getQuestionNumber()) {
            questions.add(question);
            question.setQuestionNumber(questions.indexOf(question) + 1);
        }
        questions.set(question.getQuestionNumber() - 1, question);
    }

    public void removeQuestion(Question question) {
        int questionIndex = question.getQuestionNumber() - 1;
        questions.remove(questionIndex);
        for (int i = questionIndex; i <= questions.size() - 1; i++) {
            questions.get(i).setQuestionNumber(i - 1);
        }
    }

    public void setCandidate(Candidate candidate) {
        for (Candidate c : candidates) {
            if (c.getPersonalKey().equals(candidate.getPersonalKey())) {
                candidates.set(candidates.indexOf(c), candidate);
                return;
            }
        }
        addCandidate(candidate);

    }

    public void addCandidate(Candidate candidate) {
        candidate.setTest(this);
        this.candidates.add(candidate);
    }

    public Candidate getCandidate(String personalKey) {
        for (Candidate candidate : candidates) {
            if (candidate.getPersonalKey().equals(personalKey)) {
                return candidate;
            }
        }
        return null;
    }

    public void generateCandidates(int numberOfCandidates) {
        Random r = new Random();
        int max = 9999;
        int min = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCandidates; i++) {
            sb.append(System.currentTimeMillis());
            sb.append(String.format("%04d", r.nextInt((max - min) + 1) + min));
            sb.append(i);
            sb.append(id);
            Candidate temp = new Candidate();
            temp.setPersonalKey(sb.toString());
            addCandidate(temp);
            sb.setLength(0);
        }
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
