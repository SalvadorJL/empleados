package com.mictlan.empleados.service;

import com.mictlan.empleados.entity.dtos.EmployeeRequest;
import com.mictlan.empleados.entity.dtos.EmployeeResponse;
import com.mictlan.empleados.entity.model.Employee;
import com.mictlan.empleados.exception.NoEmployeeException;
import com.mictlan.empleados.repository.EmployeeRepository;
import com.mictlan.empleados.service.impl.EmployeeImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeImplementation employeeService;

    private final Long EXISTING_ID = 1L;
    private final Long NON_EXISTING_ID = 99L;
    private Employee mockEmployee;
    private List<Employee> mockEmployees;
    private EmployeeRequest mockNewEmployee;

    @BeforeEach
    void setUp() {
        mockEmployee = new Employee(1L,"Salvador","Jeremias","Lopez","Gomez",27,"M",
                LocalDate.parse("01-01-1997", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Developer"
        );

        mockEmployees = List.of(
            mockEmployee, new Employee(2L,"Elizabeth","Maria","Gomez","Torres",25,"F",
                    LocalDate.parse("15-05-2000", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Scrum Master"
            )
        );

        mockNewEmployee = new EmployeeRequest(1L,"Salvador","Jeremias","Lopez","Gomez",27,"M",
                LocalDate.parse("01-01-1997", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Developer"
        );
    }

    @Test
    void getAllEmployeesSuccess() {
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void deleteEmployeeIfExist() {
        when(employeeRepository.existsById(EXISTING_ID)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(EXISTING_ID);
        assertDoesNotThrow(() -> {
            employeeService.deleteEmployee(EXISTING_ID);
        });

        verify(employeeRepository, times(1)).existsById(EXISTING_ID);
        verify(employeeRepository, times(1)).deleteById(EXISTING_ID);
    }

    @Test
    void deleteEmployeeIfNotExist() {
        when(employeeRepository.existsById(NON_EXISTING_ID)).thenReturn(false);
        assertThrows(NoEmployeeException.class, () -> employeeService.deleteEmployee(NON_EXISTING_ID));
        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void updateEmployeeIfExist() {
        Long id = 1L;
        EmployeeRequest employeeUpdated = new EmployeeRequest(
                null,
                "Mario",
                "Jose",
                "Santiz",
                "Lopez",
                30,
                "M",
                LocalDate.parse("01-01-1995", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                "DevOps Engineer"
        );

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = employeeService.updateEmployee(employeeUpdated, id);

        assertTrue(result);
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        assertEquals("Mario", savedEmployee.getFirstname());
        assertEquals("Santiz", savedEmployee.getFirstlastname());
        assertEquals("DevOps Engineer", savedEmployee.getPosition());

        verify(employeeRepository).findById(id);
    }

    @Test
    void updateEmployeeIfNotExist() {
        EmployeeRequest employeeUpdated = new EmployeeRequest(
                null,
                "Mario",
                "Jose",
                "Santiz",
                "Lopez",
                30,
                "M",
                LocalDate.parse("01-01-1995", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                "DevOps Engineer"
        );
        when(employeeRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(NoEmployeeException.class, () -> employeeService.updateEmployee(employeeUpdated, NON_EXISTING_ID));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void saveEmployeeSuccess() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        EmployeeResponse result = employeeService.saveEmployee(mockNewEmployee);

        assertNotNull(result);
        assertEquals(mockNewEmployee.firstname(), result.firstname());
        assertEquals(mockNewEmployee.firstlastname(), result.firstlastname());
        assertEquals(mockNewEmployee.position(), result.position());
        verify(employeeRepository).save(any(Employee.class));
    }
}