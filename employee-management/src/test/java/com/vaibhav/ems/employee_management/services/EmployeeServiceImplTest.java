package com.vaibhav.ems.employee_management.services;

import com.vaibhav.ems.employee_management.dto.EmployeeRequestDTO;
import com.vaibhav.ems.employee_management.dto.EmployeeResponseDto;
import com.vaibhav.ems.employee_management.entity.EmployeeEntity;
import com.vaibhav.ems.employee_management.exceptions.ResourceNotFoundException;
import com.vaibhav.ems.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {


    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity employeeEntity;
    private EmployeeRequestDTO employeeRequestDTO;
    private EmployeeResponseDto employeeResponseDto;
    @BeforeEach
    void setUp()
    {
        employeeEntity=new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setName("Vaibhav");

        employeeRequestDTO=new EmployeeRequestDTO();
        employeeRequestDTO.setName("Vaibhav");

        employeeResponseDto=new EmployeeResponseDto();
        employeeResponseDto.setId(1L);
        employeeResponseDto.setName("Vaibhav");
    }

    @Test
    void createEmployee_successTestCase()
    {
        Long id=1L;
        EmployeeEntity savedEmployee;
        savedEmployee=new EmployeeEntity();
        savedEmployee.setId(1L);
       // assign
        when(modelMapper.map( employeeRequestDTO,EmployeeEntity.class)).thenReturn(employeeEntity);
        when(employeeRepository.save(employeeEntity)).thenReturn(savedEmployee);
        when(modelMapper.map(savedEmployee, EmployeeResponseDto.class)).thenReturn(employeeResponseDto);
       // act
        EmployeeResponseDto result=employeeService.createEmployee(employeeRequestDTO);
       // assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Vaibhav");
        assertThat(result.getId()).isEqualTo(id);
       // verify
        verify(employeeRepository,times(1)).save(employeeEntity);
        verify(modelMapper).map(savedEmployee, EmployeeResponseDto.class);
    }
    @Test
    void createEmployee_FailureTestCase()
    {
        // assign
        when(modelMapper.map( employeeRequestDTO,EmployeeEntity.class)).thenReturn(employeeEntity);
        when(employeeRepository.save(employeeEntity)).thenThrow(new ResourceNotFoundException("DB Error"));


        // act
        RuntimeException exception= assertThrows(ResourceNotFoundException.class,()->employeeService.createEmployee(employeeRequestDTO));
        // assert
        assertThat(exception.getMessage()).isEqualTo("DB Error");
    }

    @Test
    void getEmployeeById_successTestCase()
    {
        Long id=1L;
        //assign
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employeeEntity));
        when(modelMapper.map(employeeEntity,EmployeeResponseDto.class)).thenReturn(employeeResponseDto);
        //act
        EmployeeResponseDto result=employeeService.getEmployeeById(id);
        //assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        // verify
        verify(employeeRepository,times(1)).findById(id);
        verify(modelMapper).map(employeeEntity,EmployeeResponseDto.class);
    }
    @Test
    void getEmployeeById_failureTestCase()
    {
        Long id=1L;
        // assign
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        // act
        RuntimeException exception=assertThrows(ResourceNotFoundException.class,()->employeeService.getEmployeeById(id));
        // assert
        assertThat(exception.getMessage()).isEqualTo("Employee not found with id: "+id);
        verify(employeeRepository).findById(id);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getAllEmployees_successTestCase() {

        PageRequest pageable= PageRequest.of(0,2);
        List<EmployeeEntity>entityList=List.of(employeeEntity);
        Page<EmployeeEntity> entityPage =
                new PageImpl<>(entityList, pageable, entityList.size());
        //assign
        when(employeeRepository.findAll(pageable)).thenReturn(entityPage);
        when(modelMapper.map(employeeEntity,EmployeeResponseDto.class)).thenReturn(employeeResponseDto);
        //act
        Page<EmployeeResponseDto> result =
                employeeService.getAllEmployees(pageable);
        //assert
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        //verify
        verify(employeeRepository).findAll(pageable);
        verify(modelMapper).map(employeeEntity, EmployeeResponseDto.class);
    }
    @Test
    void getAllEmployees_failureTestCase() {

        PageRequest pageable = PageRequest.of(0, 5);

        when(employeeRepository.findAll(pageable))
                .thenThrow(new ResourceNotFoundException("Database error"));

        // act & assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> employeeService.getAllEmployees(pageable)
        );

        assertThat(exception.getMessage()).isEqualTo("Database error");

        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void updateEmployee_successTestCase() {

        Long id = 1L;
        employeeRequestDTO.setStatus(true);
        // arrange
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employeeEntity));

        when(employeeRepository.save(employeeEntity))
                .thenReturn(employeeEntity);

        when(modelMapper.map(employeeEntity, EmployeeResponseDto.class))
                .thenReturn(employeeResponseDto);

        // act
        EmployeeResponseDto result =
                employeeService.updateEmployee(id,employeeRequestDTO);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        // verify
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(employeeEntity);
        verify(modelMapper).map(employeeEntity, EmployeeResponseDto.class);
    }

    @Test
    void updateEmployee_failureTestCase() {

        Long id = 1L;

        // arrange
        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        // act & assert
        RuntimeException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> employeeService.updateEmployee(id, employeeRequestDTO));

        assertThat(exception.getMessage())
                .isEqualTo("Employee not found with id:" + id);

        // verify
        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any());
        verifyNoInteractions(modelMapper);
    }


    @Test
    void deleteEmployee_successTestCase(){
        long id=1L;
        //assign
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(id);
        //act
        employeeService.deleteEmployee(id);
        //verify
        verify(employeeRepository).existsById(id);
        verify(employeeRepository).deleteById(id);
    }
    @Test
    void deleteEmployee_failureTestCase(){
        Long id=1L;
        //assign
        when(employeeRepository.existsById(id)).thenReturn(false);
        //act
        RuntimeException exception=assertThrows(ResourceNotFoundException.class,()->employeeService.deleteEmployee(id));
        //assert
        assertThat(exception.getMessage()).isEqualTo("Employee not found with id"+id);
        //verify
        verify(employeeRepository).existsById(id);
        verify(employeeRepository, never()).deleteById(any());
    }
}