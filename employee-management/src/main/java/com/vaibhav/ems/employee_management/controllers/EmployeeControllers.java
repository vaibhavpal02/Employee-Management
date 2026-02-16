package com.vaibhav.ems.employee_management.controllers;

import com.vaibhav.ems.employee_management.dto.EmployeeRequestDTO;
import com.vaibhav.ems.employee_management.dto.EmployeeResponseDto;
import com.vaibhav.ems.employee_management.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeControllers {

    private final EmployeeService employeeService;

    // Create Employee
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto createEmployee(
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        log.info("Received request to create employee with email: {}", requestDTO.getEmail());

        EmployeeResponseDto response = employeeService.createEmployee(requestDTO);

        log.info("Employee created successfully with ID: {}", response.getId());

        return response;
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public EmployeeResponseDto getEmployeeById(@PathVariable Long id) {

        log.info("Received request to fetch employee with ID: {}", id);

        EmployeeResponseDto response = employeeService.getEmployeeById(id);

        log.debug("Employee details fetched: {}", response);

        return response;
    }

    // Get All Employees (Pagination)
    @GetMapping
    public Page<EmployeeResponseDto> getAllEmployees(
            @PageableDefault(size = 3, sort = "id") Pageable pageable) {

        log.info("Fetching employees with page: {}, size: {}, sort: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<EmployeeResponseDto> response =
                employeeService.getAllEmployees(pageable);

        log.info("Total employees fetched: {}", response.getTotalElements());

        return response;
    }

    // Update Employee
    @PutMapping("/{id}")
    public EmployeeResponseDto updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        log.info("Received request to update employee with ID: {}", id);

        EmployeeResponseDto response =
                employeeService.updateEmployee(id, requestDTO);

        log.info("Employee updated successfully with ID: {}", id);

        return response;
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {

        log.warn("Received request to delete employee with ID: {}", id);

        employeeService.deleteEmployee(id);

        log.info("Employee deleted successfully with ID: {}", id);
    }
}
