package com.example.department_management_system.controller;

import com.example.department_management_system.dto.DepartmentDTO;
import com.example.department_management_system.dto.DepartmentFilterDTO;
import com.example.department_management_system.mapper.DepartmentMapper;
import com.example.department_management_system.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

///  Bolim
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<?>> getAllDepartment() {
        List<DepartmentMapper> all = departmentService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("id") Integer id) {
        DepartmentMapper byId = departmentService.getById(id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Integer id, @RequestBody DepartmentDTO departmentDTO) {
        Boolean isUpdated = departmentService.update(id, departmentDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody DepartmentDTO departmentDTO) {
        Boolean isUpdated = departmentService.updateStatus(id, departmentDTO);
        return new ResponseEntity<>(isUpdated, isUpdated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/wipe")
    public ResponseEntity<?> deleteWipe(@PathVariable("id") Integer id, @RequestBody DepartmentDTO departmentDTO) {
        Boolean isUpdate = departmentService.deleteWipe(id, departmentDTO.getVisible());
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        Boolean isUpdate = departmentService.deleteById(id);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/paged")
    public ResponseEntity<PageImpl<DepartmentMapper>> pagination(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        PageImpl<DepartmentMapper> departmentDTO = departmentService.pagination(getCurrentPage(page), size);
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<DepartmentDTO>> filter(@RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size,
                                                          @RequestBody DepartmentFilterDTO departmentFilterDTO) {
        PageImpl<DepartmentDTO> departmentDTO = departmentService.filter(departmentFilterDTO, getCurrentPage(page), size);
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

}