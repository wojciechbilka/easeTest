package com.easetest.website.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CandidateTestFormDTO {

    private int candidateId;
    private int testId;
    private String personalKey;
    @EqualsAndHashCode.Exclude
    @NotEmpty(message = "Field cannot be empty")
    private String name;
    @EqualsAndHashCode.Exclude
    @NotEmpty(message = "Field cannot be empty")
    private String surname;
    @EqualsAndHashCode.Exclude
    @Email(message = "Wrong email format.")
    @NotEmpty(message = "Field cannot be empty.")
    private String email;

    public CandidateTestFormDTO(Candidate candidate) {
        this.candidateId = candidate.getId();
        this.personalKey = candidate.getPersonalKey();
        this.name = candidate.getName();
        this.surname = candidate.getSurname();
        this.email = candidate.getEmail();
        this.testId = candidate.getTest().getId();
    }
}
