package com.mictlan.empleados.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String firstname;

    private String secondname;
    @NotNull
    @NotBlank
    private String firstlastname;

    private String secondlastname;
    @NotNull
    private Integer age;

    private String gender;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;

    @NotNull
    @NotBlank
    private String position;

}
