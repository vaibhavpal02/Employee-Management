package com.vaibhav.ems.employee_management.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeResponseDto {

    private Long id;

    private String name;

    private String email;

    private String department;

    private double salary;

    private Boolean status;

}
