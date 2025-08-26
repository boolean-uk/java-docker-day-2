package com.booleanuk.controllers;

import com.booleanuk.models.Department;
import com.booleanuk.models.Employee;
import com.booleanuk.repositories.DepartmentRepository;
import com.booleanuk.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(this.employeeRepository.findAll());
    }

    @PostMapping("{id}")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee, @PathVariable int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setDepartment(department);
        return new ResponseEntity<>(this.employeeRepository.save(employee), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getUserById(@PathVariable int id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any employee with the given id"));
        return ResponseEntity.ok(employee);
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Employee employeeToUpdate = this.employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any employee with the given id to update"));

        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());

        int departmentId = employee.getDepartment().getId();
        Department department = departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department not found"));

        employeeToUpdate.setDepartment(department);


        employeeToUpdate.setDepartment(employee.getDepartment());

        return new ResponseEntity<>(this.employeeRepository.save(employeeToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable int id) {
        Employee employeeToDelete = this.employeeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any employee with the given id to delete"));

        this.employeeRepository.delete(employeeToDelete);
        return ResponseEntity.ok(employeeToDelete);
    }

}