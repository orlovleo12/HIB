package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDTO {
    @Size(min = 5, max = 32,
            message = "Логин должен быть в диапазоне от 5 до 32")
    @Pattern(regexp = "[0-9a-zA-Z]+",
            message = "Логин не должен содержать пробелы или специальные символы")
    private String login;

    @Email
    @Pattern(regexp = "^(.+)@([a-zA-Z]+)\\.([a-zA-Z]+)$",
            message = "Email не соответствует шаблону")
    private String email;

    @Size(min = 6, max = 64, message = "пароль должен быть в диапазоне от 5 до 64")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,}$",
            message = "Пароль может содержать числа, символы в верхнем и нижнем регистрах, без пробела")
    private String password;

    private String confirmPassword;

    private String firstName;

    private String lastName;

    private long regDate;

    private long lastSeenDate;

    private String provider;

}
