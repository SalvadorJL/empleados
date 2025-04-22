package com.mictlan.empleados.repository;

import com.mictlan.empleados.entity.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {
}
