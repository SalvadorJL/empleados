package com.mictlan.empleados.service;

import com.mictlan.empleados.entity.dtos.EmployeeRequest;
import com.mictlan.empleados.entity.dtos.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    public List<EmployeeResponse> getAllEmployees();
    public boolean deleteEmployee(Long id);
    public boolean updateEmployee(EmployeeRequest employeeRequest, Long id);
    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest);
}
