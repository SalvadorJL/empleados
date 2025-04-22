package com.mictlan.empleados.controller;

import com.mictlan.empleados.entity.dtos.EmployeeRequest;
import com.mictlan.empleados.entity.dtos.GeneralResponse;
import com.mictlan.empleados.service.EmployeeService;
import com.mictlan.empleados.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Constants.API_BASE_PATH)
@Tag(name = "Employee API", description = "Employee backend API")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "Employee list")
    public ResponseEntity<GeneralResponse> getAllEmpleados() {
        var employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Employee list")
                .status(HttpStatus.OK.value())
                .data(employees)
                .build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete employee by id")
    public ResponseEntity<GeneralResponse> deleteEmpleado(@PathVariable Long id) {
        var employeeDeleted = employeeService.deleteEmployee(id);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Employee deleted")
                .status(HttpStatus.OK.value())
                .data(employeeDeleted)
                .build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "update employee by id")
    public ResponseEntity<GeneralResponse> updateEmpleado(@RequestBody EmployeeRequest employeeRequest, @PathVariable Long id) {
        var employeeUpdated = employeeService.updateEmployee(employeeRequest, id);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Employee updated")
                .status(HttpStatus.OK.value())
                .data(employeeUpdated)
                .build());
    }

    @PostMapping
    @Operation(summary = "save employee")
    public ResponseEntity<GeneralResponse> saveEmpleado(@RequestBody EmployeeRequest employeeRequest) {
        var employeeSaved = employeeService.saveEmployee(employeeRequest);
        URI location = URI.create(Constants.API_BASE_PATH + "/save/" + employeeSaved.id());
        return ResponseEntity.created(location)
                .body(
                    GeneralResponse.builder()
                    .message("Employee saved")
                    .status(HttpStatus.CREATED.value())
                    .data(employeeSaved)
                    .build()
                );
    }
}
