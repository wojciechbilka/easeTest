package com.easetest.website.model;


import com.easetest.website.security.constraint.FieldMatch;
import com.easetest.website.security.constraint.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    //TODO error message in english, not polish

    @Size(min = 3, max = 10, message = "Username length must contain in range 3-10 letters.")
    private String userName;

    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmPassword;

    @Email(message = "Wrong email format.")
    @NotEmpty(message = "Field cannot be empty")
    private String email;

    @Email(message = "Wrong email format.")
    @NotEmpty(message = "Field cannot be empty")
    private String confirmEmail;

    public User createUser() {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setEmail(email);
        user.setActive(true);
        user.setRoles("ROLE_USER");
        return user;
    }
}
