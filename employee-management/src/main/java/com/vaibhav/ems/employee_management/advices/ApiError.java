package com.vaibhav.ems.employee_management.advices;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Builder
@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;
}
