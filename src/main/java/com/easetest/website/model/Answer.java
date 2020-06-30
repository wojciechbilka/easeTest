package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @EqualsAndHashCode.Exclude
    private int answerNumber;
    @EqualsAndHashCode.Exclude
    private String text;
    @EqualsAndHashCode.Exclude
    private boolean correct;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer(int answerNumber, String text, boolean correct) {
        this.answerNumber = answerNumber;
        this.text = text;
        this.correct = correct;
    }

    public Answer(int answerNumber, String text, boolean correct, Question question) {
        this.answerNumber = answerNumber;
        this.text = text;
        this.correct = correct;
        this.question = question;
    }
}
