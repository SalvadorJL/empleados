package com.mictlan.empleados.entity.dtos;

import jakarta.validation.constraints.AssertFalse;
import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GeneralResponse<T> {
    private String message;
    private Integer status;
    private T data;
}
