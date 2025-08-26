package com.booleanuk.api.controller;

import com.booleanuk.api.model.Department;
import com.booleanuk.api.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departments")
public class DepartmentController {


    @Autowired private DepartmentRepository departmentRepository;



    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(){

        return new ResponseEntity<>(departmentRepository.findAll(), HttpStatus.OK);
    }


    @PutMapping("{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department department){

        Department departmentToUpdate = departmentRepository.findById(id).orElseThrow(()->new RuntimeException("notFound"));
        departmentToUpdate.setName(department.getName());
        departmentToUpdate.setLocation(department.getLocation());

        return new ResponseEntity<>(departmentRepository.save(departmentToUpdate), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department){

        return new ResponseEntity<>(departmentRepository.save(department), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Department> createDepartment(@PathVariable int id){

        Department department = departmentRepository.findById(id).orElseThrow(()->new RuntimeException("notFound"));
        departmentRepository.delete(department);

        return new ResponseEntity<>(department,HttpStatus.OK);
    }


}
