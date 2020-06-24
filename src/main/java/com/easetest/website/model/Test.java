package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @EqualsAndHashCode.Exclude
    @NotEmpty(message = "Field cannot be empty.")
    private String testName;
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "test_questions_mapping",
            joinColumns = {@JoinColumn(name = "test_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")})
    @MapKey(name = "questionNumber")
    private Map<Integer, Question> questions;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter owner;

    public void addQuestion(Question question) {
        if(questions == null) {
            questions = new HashMap<>();
        }
        questions.put(question.getQuestionNumber(), question);
    }

    public void removeQuestion(Question question) {
        if(questions == null) {
            return;
        }
        questions.remove(question.getQuestionNumber());
    }
}
