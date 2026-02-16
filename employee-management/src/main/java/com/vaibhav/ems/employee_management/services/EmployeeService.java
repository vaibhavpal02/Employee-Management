package com.vaibhav.ems.employee_management.services;

import com.vaibhav.ems.employee_management.dto.EmployeeRequestDTO;
import com.vaibhav.ems.employee_management.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDto createEmployee(EmployeeRequestDTO requestDTO);

    EmployeeResponseDto getEmployeeById(Long id);


    Page<EmployeeResponseDto> getAllEmployees(Pageable pageable);


    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDTO requestDTO);

    void deleteEmployee(Long id);
}

