package com.mictlan.empleados.entity.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record EmployeeRequest(
        Long id,
        String firstname,
        String secondname,
        String firstlastname,
        String secondlastname,
        Integer age,
        String gender,
        @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthdate,
        String position){}
