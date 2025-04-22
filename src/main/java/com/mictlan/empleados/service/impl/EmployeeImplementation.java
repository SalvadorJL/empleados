package com.mictlan.empleados.service.impl;

import com.mictlan.empleados.entity.dtos.EmployeeRequest;
import com.mictlan.empleados.entity.dtos.EmployeeResponse;
import com.mictlan.empleados.entity.model.Employee;
import com.mictlan.empleados.exception.BadRequestException;
import com.mictlan.empleados.exception.NoEmployeeException;
import com.mictlan.empleados.repository.EmployeeRepository;
import com.mictlan.empleados.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeImplementation implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    public EmployeeImplementation(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        var employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            throw new NoEmployeeException("Employee list is empty");
        }

        return employees.stream().map(this::mapToEmployeesResponse).toList();
    }

    @Override
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        } else {
            throw new NoEmployeeException("Employee with ID: " + id + " not found");
        }
    }

    @Override
    public boolean updateEmployee(EmployeeRequest employeeRequest, Long id) {
        validateRequest(employeeRequest);
        return employeeRepository.findById(id)
                .map(existing -> {
                    var updatedEmployee = mapRequestToEmployee(employeeRequest);
                    updatedEmployee.setId(id);
                    employeeRepository.save(updatedEmployee);
                    return true;
                })
                .orElseThrow(() -> new NoEmployeeException("Employee with ID: " + id + " not found"));
    }

    @Override
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) {
        validateRequest(employeeRequest);
        var empleadoResponse = employeeRepository.save(mapRequestToEmployee(employeeRequest));
        return mapToEmployeesResponse(empleadoResponse);
    }

    private Employee mapRequestToEmployee(EmployeeRequest employeeRequest) {
        return Employee.builder()
                .firstname(employeeRequest.firstname())
                .secondname(employeeRequest.secondname())
                .firstlastname(employeeRequest.firstlastname())
                .secondlastname(employeeRequest.secondlastname())
                .age(employeeRequest.age())
                .gender(employeeRequest.gender())
                .birthdate(employeeRequest.birthdate())
                .position(employeeRequest.position())
                .build();
    }

    private EmployeeResponse mapToEmployeesResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstname(),
                employee.getSecondname(),
                employee.getFirstlastname(),
                employee.getSecondlastname(),
                employee.getAge(),
                employee.getGender(),
                employee.getBirthdate(),
                employee.getPosition()
        );
    }

    private void validateRequest(EmployeeRequest employeeRequest) {
        if (employeeRequest.firstname() == null || employeeRequest.firstname().isBlank()) {
            throw new BadRequestException("The first name cannot be empty");
        }
        if (employeeRequest.firstlastname() == null || employeeRequest.firstlastname().isBlank()) {
            throw new BadRequestException("The first last name cannot be empty");
        }
        if (employeeRequest.age() == null || employeeRequest.age() <= 0) {
            throw new BadRequestException("The age is required and must be greater than 0");
        }
        if (employeeRequest.birthdate() == null) {
            throw new BadRequestException("The birthdate is required");
        }
        if (employeeRequest.position() == null || employeeRequest.position().isBlank()) {
            throw new BadRequestException("The position cannot be empty");
        }
    }
}
