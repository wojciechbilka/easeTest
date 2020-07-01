package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @EqualsAndHashCode.Exclude
    private String personalKey;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String surname;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime completedDate;
    @EqualsAndHashCode.Exclude
    private boolean completed= false;
    @ElementCollection
    private List<String> results = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    public Candidate(CandidateTestFormDTO candidateTestFormDTO) {
        this.setId(candidateTestFormDTO.getCandidateId());
        this.setPersonalKey(candidateTestFormDTO.getPersonalKey());
        this.setName(candidateTestFormDTO.getName());
        this.setSurname(candidateTestFormDTO.getSurname());
        this.setEmail(candidateTestFormDTO.getEmail());
    }
}
