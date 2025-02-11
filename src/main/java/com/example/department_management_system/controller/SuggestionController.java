package com.example.department_management_system.controller;


import com.example.department_management_system.dto.SuggestionDTO;
import com.example.department_management_system.dto.SuggestionFilterDTO;
import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.SuggestionMapper;
import com.example.department_management_system.service.SuggestionService;
import com.example.department_management_system.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


///  Bo'limga kelgan takliflars
@RestController
@RequestMapping("/suggestion")
public class SuggestionController {
    @Autowired
    public SuggestionService suggestionService;

    // Create suggestion
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("")
    public SuggestionDTO createSuggestion(@RequestBody SuggestionDTO suggestionDTO) {
        return suggestionService.create(suggestionDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<SuggestionMapper>> getAllSuggestion() {
        List<SuggestionMapper> getAll = suggestionService.getAll();
        return ResponseEntity.ok().body(getAll);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SuggestionMapper> getSuggestionById(@PathVariable("id") Integer id) {
        SuggestionMapper byId = suggestionService.getById(id);
        return ResponseEntity.ok().body(byId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/createdBy")
    public ResponseEntity<List<SuggestionMapper>> findByCreatedBy() {
        List<SuggestionMapper> createdBy = suggestionService.findByCreatedBy();
        return ResponseEntity.ok().body(createdBy);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/suggestion")
    public ResponseEntity<?> updateSuggestion(@PathVariable("id") Integer id, @RequestBody SuggestionDTO suggestionDTO) {
        Boolean updated = suggestionService.updateSuggestion(id, suggestionDTO);
        return ResponseEntity.ok().body(updated);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PatchMapping("/{id}/wipe")
    public ResponseEntity<?> deleteWipe(@PathVariable("id") Integer id, @RequestBody Boolean visible) {
        Boolean isUpdate = suggestionService.deleteWipe(id, visible);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        Boolean isUpdate = suggestionService.deleteById(id);
        return new ResponseEntity<>(isUpdate, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/paged")
    public ResponseEntity<PageImpl<SuggestionMapper>> pagination(@RequestParam("page") int page,
                                                            @RequestParam("size") int size) {
        PageImpl<SuggestionMapper> suggestionDTOS = suggestionService.pagination(getCurrentPage(page), size);
        return new ResponseEntity<>(suggestionDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<SuggestionDTO>> filter(@RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "30") int size,
                                                        @RequestBody SuggestionFilterDTO suggestionFilterDTO) {
        PageImpl<SuggestionDTO> departmentDTO = suggestionService.filter(suggestionFilterDTO, getCurrentPage(page), size);
        return new ResponseEntity<>(departmentDTO, HttpStatus.OK);
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

    private void checkAdminAccess() {
        if (!SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
    }
}