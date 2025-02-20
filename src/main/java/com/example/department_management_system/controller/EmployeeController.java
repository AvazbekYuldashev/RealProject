package com.example.department_management_system.controller;

import com.example.department_management_system.dto.EmployeeDTO;
import com.example.department_management_system.dto.EmployeeFilterDTO;
import com.example.department_management_system.dto.EmployeeUpdateDTO;
import com.example.department_management_system.mapper.EmployeeMapper;
import com.example.department_management_system.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

///  Xodimlar
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    ///  Create

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    ///  Get all

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeMapper>> getAll() {
        List<EmployeeMapper> all = employeeService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
    ///  Get By Id

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMapper> getById(@PathVariable("id") Integer id) {
        EmployeeMapper byId = employeeService.getById(id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/getMe")
    public ResponseEntity<EmployeeMapper> getMe() {
        EmployeeMapper byId = employeeService.getMe();
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    ///  Get by department Id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/department")
    public ResponseEntity<List<EmployeeMapper>> getAllByDepartment(@PathVariable("id") Integer id) {
        List<EmployeeMapper> byDepartment = employeeService.getByDepartmentId(id);
        return ResponseEntity.ok(byDepartment);
    }

    ///  Update role
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<Boolean> updateRole(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        Boolean isUpdated = employeeService.updateRole(id, employeeDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    ///  Update position
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/position")
    public ResponseEntity<Boolean> updatePosition(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        Boolean isUpdated = employeeService.updatePosition(id, employeeDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    ///  Update Status
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Boolean> updateStatus(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        Boolean isUpdated = employeeService.updateStatus(id, employeeDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /// Update department
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/department")
    public ResponseEntity<Boolean> updateDepartment(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        Boolean isUpdated = employeeService.updateDepartment(id, employeeDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /// Update
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateEmployee(@Valid @PathVariable Integer id, @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        Boolean isUpdated = employeeService.updateEmployee(id, employeeUpdateDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/wipe")
    public ResponseEntity<?> deleteWipeA(@PathVariable("id") Integer id, @RequestBody EmployeeFilterDTO employeeDTO) {
        Boolean isUpdate = employeeService.deleteWipeA(id, employeeDTO.getVisible());
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/wipe")
    public ResponseEntity<?> deleteWipe() {
        Boolean isUpdate = employeeService.deleteWipe();
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> updateVisible(@PathVariable("id") Integer id) {
        Boolean isUpdate = employeeService.deleteById(id);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/paged")
    public ResponseEntity<PageImpl<EmployeeMapper>> pagination(@RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        PageImpl<EmployeeMapper> employeeDTOS = employeeService.pagination(getCurrentPage(page), size);
        return new ResponseEntity<>(employeeDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<EmployeeDTO>> filter(@RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size,
                                                          @RequestBody EmployeeFilterDTO employeeFilterDTO) {
        PageImpl<EmployeeDTO> departmentDTO = employeeService.filter(employeeFilterDTO, getCurrentPage(page), size);
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }
}
