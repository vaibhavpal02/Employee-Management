package com.vaibhav.ems.employee_management.services;

import com.vaibhav.ems.employee_management.dto.EmployeeRequestDTO;
import com.vaibhav.ems.employee_management.dto.EmployeeResponseDto;
import com.vaibhav.ems.employee_management.entity.EmployeeEntity;
import com.vaibhav.ems.employee_management.exceptions.ResourceNotFoundException;
import com.vaibhav.ems.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDTO requestDTO) {
        log.info("Creating Employee with name {}",requestDTO.getName());
        EmployeeEntity entity=modelMapper.map(requestDTO,EmployeeEntity.class);
        EmployeeEntity savedEmployee =employeeRepository.save(entity);
        log.info("Employee created Successfully with ID {}",savedEmployee.getId());
        return modelMapper.map(savedEmployee, EmployeeResponseDto.class);
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.info("Fetching Employee with ID {}", id);
        EmployeeEntity employeeEntity=employeeRepository.findById(id).
                orElseThrow(()->{
                    log.error("Employee not found with ID {}", id);
            return new ResourceNotFoundException("Employee not found with id: "+id);
                });
        log.info("Employee found successfully with ID {}", id);
        return modelMapper.map(employeeEntity,EmployeeResponseDto.class);
    }

    @Override
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.info("Fetching Page Number:{} -Page Size:{}",pageable.getPageNumber(),pageable.getPageSize());
        Page<EmployeeEntity> page=employeeRepository.findAll(pageable);
        log.info("Total employees found: {}", page.getTotalElements());
        return page.map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeResponseDto.class));
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        log.info("Fetching id{}",id);
        EmployeeEntity employeeEntity=employeeRepository.findById(id).orElseThrow(()->
                {
                        log.info("Employee not found with id{}",id);
                        return new ResourceNotFoundException("Employee not found with id:"+id);
                }
        );
        log.debug("Old employee data- Name:{}, Email:{},Department:{},Salary:{}",employeeEntity.getName(),employeeEntity.getEmail(),employeeEntity.getDepartment(),employeeEntity.getSalary());
        employeeEntity.setName(requestDTO.getName());
        employeeEntity.setEmail(requestDTO.getEmail());
        employeeEntity.setDepartment(requestDTO.getDepartment());
        employeeEntity.setSalary(requestDTO.getSalary());
        employeeEntity.setStatus(requestDTO.getStatus());
        EmployeeEntity updated= employeeRepository.save(employeeEntity);

        log.info("Employee updated successfuly with id{}",id);

        return modelMapper.map(updated,EmployeeResponseDto.class);


    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Attempting to delete employee with ID: {}", id);
        if(!employeeRepository.existsById(id))
        {
            log.error("Delete failed. Employee not found with ID: {}", id);
            throw new ResourceNotFoundException("Employee not found with id"+id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with ID: {}", id);
    }
}
