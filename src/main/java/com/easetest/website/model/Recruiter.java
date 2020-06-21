package com.easetest.website.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recruiter {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @EqualsAndHashCode.Exclude
    private String firstName;
    @EqualsAndHashCode.Exclude
    private String lastName;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    private String company;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private List<Test> testList;

    public void addTest(Test test) {
        if(testList == null) {
            testList = new ArrayList<>();
        }
        testList.add(test);
        test.setOwner(this);
    }

    public void removeTest(Test test) {
        if(testList == null) {
            return;
        }
        testList.remove(test);
        test.setOwner(null);
    }
}
