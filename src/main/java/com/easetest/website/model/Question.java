package com.easetest.website.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private Integer questionNumber;
    private String questionBody;

    @ElementCollection
    @CollectionTable(name = "question_answers_mapping",
            joinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "answer_number")
    @Column(name = "answer")
    private Map<Integer, String> answers;

    public void addAnswer(Integer num, String answer) {
        if(answers == null) {
            answers = new HashMap<>();
        }
        answers.put(num, answer);
    }

    public void removeAnswer(Integer num) {
        if(answers == null) {
            return;
        }
        answers.remove(num);
    }

}
