package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

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
    private String questionBody;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    //TODO add orphan removal (adding new answer in case of editing)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "question_answers_mapping",
            joinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "answer_id", referencedColumnName = "id")})
    @MapKey(name = "answerNumber")
    private Map<Integer, Answer> answers = new HashMap<>();


    public void setAnswer(Integer num, String answer, boolean correct) {
        if(answers == null) {
            answers = new HashMap<>();
        }
        Answer a = answers.get(num) != null ? answers.get(num) : new Answer();
        a.setAnswerNumber(num);
        a.setText(answer);
        a.setCorrect(correct);
        answers.put(num, a);
    }

    public void removeAnswer(Integer num) {
        if(answers == null) {
            return;
        }
        answers.remove(num);
    }

}
