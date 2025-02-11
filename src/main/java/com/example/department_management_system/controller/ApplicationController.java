package com.example.department_management_system.controller;


import com.example.department_management_system.dto.ApplicationDTO;
import com.example.department_management_system.dto.ApplicationFilterDTO;
import com.example.department_management_system.enums.ApplicationStatus;
import com.example.department_management_system.mapper.ApplicationMapper;
import com.example.department_management_system.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Arizalar
@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /// Create
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationDTO applicationDTO) {
        ApplicationDTO savedApplication = applicationService.saveApplication(applicationDTO);
        return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
    }
    /// Get all
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    private ResponseEntity<List<ApplicationMapper>> getAll(){
        List<ApplicationMapper> applicationMapper = applicationService.getAll();
        return new ResponseEntity<>(applicationMapper, HttpStatus.OK);
    }
    /// Get by Id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationMapper> getApplicationById(@PathVariable("id") Integer id) {
        ApplicationMapper applicationDTO = applicationService.getById(id);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }

    /// Get by sender Id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/createdBy")
    public ResponseEntity<List<ApplicationMapper>> getByCreatedById() {
        List<ApplicationMapper> applicationDTO = applicationService.findByCreatedBy();
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
    /// Get by getter ID
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/assignedTo")
    public ResponseEntity<List<ApplicationMapper>> getByAssignedToId() {
        List<ApplicationMapper> applicationDTO = applicationService.findByAssignedTo();
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
    ///  Get by department
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/department")
    public ResponseEntity<List<ApplicationMapper>> getBydepartmentId(@PathVariable("id") Integer id) {
        List<ApplicationMapper> applicationDTO = applicationService.findByDepartmentId(id);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
    /// Get by status
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{status}/status")
    public ResponseEntity<List<ApplicationMapper>> getByStatus(@PathVariable("status") ApplicationStatus status) {
        List<ApplicationMapper> applicationDTO = applicationService.findByStatus(status);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
    /// Get by service Id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/assignedTo")
    public ResponseEntity<?> updateAssignedTo(@PathVariable("id") Integer id, @RequestBody ApplicationDTO applicationDTO) {
        Boolean isUpdate = applicationService.updateAssignedTo(id, applicationDTO);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }
    /// Update Status
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody ApplicationDTO applicationDTO) {
        Boolean isUpdate = applicationService.updateStatus(id, applicationDTO);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }
    /// Update application
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable("id") Integer id, @RequestBody ApplicationDTO applicationDTO) {
        Boolean isUpdate = applicationService.updateApplication(id, applicationDTO);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }
    /// Delete for visible
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PatchMapping("/{id}/wipe")
    public ResponseEntity<?> updateVisible(@PathVariable("id") Integer id, @RequestBody Boolean visible) {
        Boolean isUpdate = applicationService.updateVisible(id, visible);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }
    /// Delete By Id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> updateVisible(@PathVariable("id") Integer id) {
        Boolean isUpdate = applicationService.deleteById(id);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }
    /// Pagination
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/paged/admins")
    public ResponseEntity<PageImpl<ApplicationMapper>> pagination(@RequestParam("page") int page,
                                                            @RequestParam("size") int size) {
        PageImpl<ApplicationMapper> applicationDTOS = applicationService.pagination(getCurrentPage(page), size);
        return new ResponseEntity<>(applicationDTOS, HttpStatus.OK);
    }
    ///  Filter
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<ApplicationDTO>> filter(@RequestParam(value = "page", defaultValue = "1") int page,
                                                             @RequestParam(value = "size", defaultValue = "30") int size,
                                                             @RequestBody ApplicationFilterDTO applicationFilterDTO) {
        PageImpl<ApplicationDTO> applicationDTO = applicationService.filter(applicationFilterDTO, getCurrentPage(page), size);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/paged")
    public ResponseEntity<PageImpl<ApplicationMapper>> getCreatedByPaged(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size) {
        PageImpl<ApplicationMapper> applicationDTOS = applicationService.getCreatedByPaged(getCurrentPage(page), size);
        return new ResponseEntity<>(applicationDTOS, HttpStatus.OK);
    }
}
