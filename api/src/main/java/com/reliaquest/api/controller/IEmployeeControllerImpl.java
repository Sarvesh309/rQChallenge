package com.reliaquest.api.controller;

import com.reliaquest.api.service.EmployeeService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.reliaquest.api.service.EmployeeService.getAllEmployee;

@RestController
@RequestMapping("/api/v1/employee")
public class IEmployeeControllerImpl<Entity, Input> implements IEmployeeController<Entity, Input> {

    @Override
    @GetMapping
    public ResponseEntity<List<Entity>> getAllEmployees() {
            return ResponseEntity.ok((List<Entity>) getAllEmployee());
    }

    /**
     * @param searchString
     * @return
     */
    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Entity>> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
        return ResponseEntity.ok((List<Entity>) EmployeeService.getEmployeesByNameSearch(searchString));
    }

    /**
     * @param id
     * @return
     */
    @Override
    @GetMapping("id/{id}")
    public ResponseEntity<Entity> getEmployeeById(@PathVariable("id") String id) {
        return (ResponseEntity<Entity>) ResponseEntity.ok(EmployeeService.getEmployeeById(id));
    }

    /**
     * @return Highest salary of the employee
     */
    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(EmployeeService.getHighestSalaryOfEmployees());
    }

    /**
     * {@code @returns} Names of top 10 Salaried Employees with Salaries sorted in Descending Order
     */
    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(EmployeeService.getTopTenHighestEarningEmployeeNames());
    }

    /**
     * @param employeeInput
     * {@code @returns} Employee created with given input
     */
    @Override
    @PostMapping
    public ResponseEntity<Entity> createEmployee(@RequestBody Input employeeInput) {
        return (ResponseEntity<Entity>) ResponseEntity.ok(EmployeeService.createEmployee(employeeInput));
    }

    /**
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
            return ResponseEntity.ok(EmployeeService.deleteEmployeeById(id));
    }
}