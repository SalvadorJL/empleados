package com.mictlan.empleados.entity.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record EmployeeResponse (
        @JsonProperty("id") Long id,
        @JsonProperty("firstname") String firstname,
        @JsonProperty("secondname") String secondname,
        @JsonProperty("firstlastname") String firstlastname,
        @JsonProperty("secondlastname") String secondlastname,
        @JsonProperty("age") int age,
        @JsonProperty("gender") String gender,
        @JsonProperty("birthdate") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthdate,
        @JsonProperty("position")String position){}
