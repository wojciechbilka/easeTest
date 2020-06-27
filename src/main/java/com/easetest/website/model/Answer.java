package com.easetest.website.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


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

    public Answer(int answerNumber, String text, boolean correct) {
        this.answerNumber = answerNumber;
        this.text = text;
        this.correct = correct;
    }





}
