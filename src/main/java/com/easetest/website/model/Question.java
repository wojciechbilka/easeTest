package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @EqualsAndHashCode.Exclude
    private int questionNumber;

    @EqualsAndHashCode.Exclude
    private String questionBody = "";

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", orphanRemoval = true)
    private List<Answer> answers = Arrays.asList(
            new Answer(1, "", false, this),
            new Answer(2, "", false, this),
            new Answer(3, "", false, this),
            new Answer(4, "", false, this));

    public Question(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setAnswer(Integer num, String answer, boolean correct) {
        if(answers == null) {
            answers = new ArrayList<>();
        }
        Answer a = answers.get(num - 1) != null ? answers.get(num - 1) : new Answer();
        a.setAnswerNumber(num);
        a.setText(answer);
        a.setCorrect(correct);
        a.setQuestion(this);
        answers.set(num-1, a);
    }

    public void setAnswer(Answer answer) {
        setAnswer(answer.getAnswerNumber(), answer.getText(), answer.isCorrect());
    }

    public void removeAnswer(Integer num) {
        if(answers == null) {
            return;
        }
        answers.set(num-1, new Answer(num, "", false, this));
    }

}
