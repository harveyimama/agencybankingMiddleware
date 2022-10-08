package com.boat.bp.middleware.rest.value;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

@Value
public class OnboardingRequestValue {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd/MM/yyyy"
    )
    private Date dateOfBirth;
    private Gender gender;
    private String userStatus;
    private Role role;
}
