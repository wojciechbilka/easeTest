package com.easetest.website.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    int id;
    @EqualsAndHashCode.Exclude
    private String userName;
    @EqualsAndHashCode.Exclude
    private String password;
    @EqualsAndHashCode.Exclude
    private boolean active;
    @EqualsAndHashCode.Exclude
    private String roles;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Recruiter recruiter;

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
        recruiter.setUser(this);
    }
}
