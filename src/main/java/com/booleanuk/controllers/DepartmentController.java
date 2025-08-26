package com.booleanuk.controllers;

import com.booleanuk.models.Department;
import com.booleanuk.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("departments")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(this.departmentRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(this.departmentRepository.save(department), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        Department department = this.departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any department with the given id"));
        return ResponseEntity.ok(department);
    }

    @PutMapping("{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department department) {
        Department departmentToUpdate = this.departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any department with the given id to update"));

        departmentToUpdate.setName(department.getName());
        departmentToUpdate.setLocation(department.getLocation());

        return new ResponseEntity<>(this.departmentRepository.save(departmentToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable int id) {
        Department departmentToDelete = this.departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find any department with the given id to delete"));

        this.departmentRepository.delete(departmentToDelete);
        return ResponseEntity.ok(departmentToDelete);
    }
}