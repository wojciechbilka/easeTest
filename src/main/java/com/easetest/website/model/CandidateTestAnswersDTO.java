package com.easetest.website.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateTestAnswersDTO {

    private int candidateId;
    private int testId;
    private int questionId;
    private Question question;
    private boolean aChecked;
    private boolean bChecked;
    private boolean cChecked;
    private boolean dChecked;

    public void updateCandidateAnswers(Candidate candidate) {
        StringBuilder sb = new StringBuilder();
        List<String> results = candidate.getResults();
        int questionIndex = getQuestion().getQuestionNumber() - 1;
        sb.append(getQuestion().getQuestionNumber());
        if(aChecked) {
            sb.append("a");
        }
        if(bChecked) {
            sb.append("b");
        }
        if(cChecked) {
            sb.append("c");
        }
        if(dChecked) {
            sb.append("d");
        }
        if(results.size() > questionIndex && results.get(questionIndex) != null) {
            results.set(questionIndex, sb.toString());
        } else {
            results.add(questionIndex, sb.toString());
        }
    }

    public void setCandidateAnswersForQuestion(Question question, Candidate candidate) {
        List<String> results = candidate.getResults();
        if(results.size() < question.getQuestionNumber()) {
            this.question = question;
            this.aChecked = false;
            this.bChecked = false;
            this.cChecked = false;
            this.dChecked = false;
        } else {
            String answer = results.get(question.getQuestionNumber() - 1);
            this.question = question;
            this.aChecked = answer.contains("a");
            this.bChecked = answer.contains("b");
            this.cChecked = answer.contains("c");
            this.dChecked = answer.contains("d");
        }
    }

    public void setQuestion(Question question) {
        this.question = question;
        questionId = question.getId();
    }
}
