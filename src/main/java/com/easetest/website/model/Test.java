package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    private String testName;
    @EqualsAndHashCode.Exclude
    private LocalDateTime startTime;
    @EqualsAndHashCode.Exclude
    private int testTime; //minutes
    @EqualsAndHashCode.Exclude
    private boolean multipleAnswers;
    @EqualsAndHashCode.Exclude
    private int numberOfQuestions;

    //mapa pytań (możliwość losowania z całej puli/mapy, stąd pole numberOfQuestions)
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
