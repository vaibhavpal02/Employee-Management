package com.vaibhav.ems.employee_management.repository;

import com.vaibhav.ems.employee_management.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

}
