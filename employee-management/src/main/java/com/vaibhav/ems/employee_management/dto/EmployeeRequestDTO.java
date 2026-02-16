package com.vaibhav.ems.employee_management.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeRequestDTO {



    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String department;

    @Positive
    @NotNull
    private double salary;

    @NotNull
    private Boolean status;

}
