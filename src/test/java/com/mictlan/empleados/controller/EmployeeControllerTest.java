package com.mictlan.empleados.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mictlan.empleados.EmpleadosApplication;
import com.mictlan.empleados.entity.dtos.EmployeeRequest;
import com.mictlan.empleados.entity.dtos.EmployeeResponse;
import com.mictlan.empleados.exception.BadRequestException;
import com.mictlan.empleados.exception.NoEmployeeException;
import com.mictlan.empleados.service.EmployeeService;
import com.mictlan.empleados.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(EmployeeController.class)
@Import(EmpleadosApplication.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private EmployeeResponse mockEmployee;
    private EmployeeRequest mockNewEmployee;
    private EmployeeRequest mockUpdateEmployee;
    private List<EmployeeResponse> mockEmployees;
    private ObjectMapper objectMapper;

    private final Long EXISTING_ID = 1L;
    private final Long NON_EXISTING_ID = 99L;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));

        mockEmployee = new EmployeeResponse(1L,"Salvador","Jeremias","Lopez","Gomez",27,"M",
                LocalDate.parse("01-01-1997", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Developer"
        );

        mockEmployees = List.of(
                mockEmployee, new EmployeeResponse(null,"Elizabeth","Maria","Gomez","Torres",25,"F",
                        LocalDate.parse("15-05-2000", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Scrum Master"
                )
        );

        mockUpdateEmployee = new EmployeeRequest(1L,"Salvador","Jeremias","Lopez","Gomez",27,"M",
                LocalDate.parse("01-01-1997", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Developer"
        );

        mockNewEmployee = new EmployeeRequest(null,"Salvador","Jeremias","Lopez","Gomez",27,"M",
                LocalDate.parse("01-01-1997", DateTimeFormatter.ofPattern("dd-MM-yyyy")),"Developer"
        );
    }

    @Test
    void getAllEmployees_Controller() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get(Constants.API_BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(mockEmployees.size())))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void deleteEmployeeIfExist_Controller() throws Exception {
        when(employeeService.deleteEmployee(EXISTING_ID)).thenReturn(true);

        mockMvc.perform(delete(Constants.API_BASE_PATH + "/delete/" + EXISTING_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(employeeService, times(1)).deleteEmployee(EXISTING_ID);
    }

    @Test
    void deleteEmployeeIfNotExist_Controller() throws Exception {
        doThrow(new NoEmployeeException(Constants.EMPLOYEE_NOT_FOUND + NON_EXISTING_ID))
                .when(employeeService).deleteEmployee(NON_EXISTING_ID);

        mockMvc.perform(delete(Constants.API_BASE_PATH + "/delete/" + NON_EXISTING_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));

        verify(employeeService, times(1)).deleteEmployee(NON_EXISTING_ID);
    }

    @Test
    void updateEmployeeValidParameter_Controller() throws Exception {

        when(employeeService.updateEmployee(mockUpdateEmployee, EXISTING_ID)).thenReturn(true);

        mockMvc.perform(put(Constants.API_BASE_PATH + "/update/" + EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUpdateEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));

        verify(employeeService, times(1)).updateEmployee(mockUpdateEmployee, EXISTING_ID);
    }

    @Test
    void updateEmployeeNotValidParameter_Controller() throws Exception {
        EmployeeRequest invalidEmployee = new EmployeeRequest(1L, "", "", "", "", 0, "", null, "");

        doThrow(new BadRequestException("Invalid Data"))
                .when(employeeService).updateEmployee(any(), anyLong());

        mockMvc.perform(put(Constants.API_BASE_PATH + "/update/" + EXISTING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));

        verify(employeeService, times(1)).updateEmployee(invalidEmployee, EXISTING_ID);
    }

    @Test
    void saveEmployee_Controller() throws Exception {
        when(employeeService.saveEmployee(any(EmployeeRequest.class))).thenReturn(mockEmployee);

        mockMvc.perform(post(Constants.API_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockNewEmployee)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstname").value("Salvador"))
                .andExpect(jsonPath("$.data.firstlastname").value("Lopez"))
                .andExpect(jsonPath("$.data.birthdate").value("01-01-1997"));

        verify(employeeService).saveEmployee(any(EmployeeRequest.class));
    }
}