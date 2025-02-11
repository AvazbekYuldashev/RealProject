package com.example.department_management_system.service;

import com.example.department_management_system.dto.SuggestionDTO;
import com.example.department_management_system.dto.SuggestionFilterDTO;
import com.example.department_management_system.entity.SuggestionEntity;
import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.SuggestionMapper;
import com.example.department_management_system.repository.SuggestionCustomRepository;
import com.example.department_management_system.repository.SuggestionRepository;
import com.example.department_management_system.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {

    @Autowired
    @Lazy
    private SuggestionRepository suggestionRepository;
    @Autowired
    @Lazy
    private EmployeeService employeeService;
    @Autowired
    @Lazy
    private DepartmentService departmentService;
    @Autowired
    private SuggestionCustomRepository suggestionCustomRepository;

    /// Create
    public SuggestionDTO create(SuggestionDTO suggestionDTO) {
        suggestionDTO.setCreatedDate(LocalDateTime.now());
        suggestionDTO.setUpdatedDate(LocalDateTime.now());
        suggestionDTO.setVisible(true);
        suggestionDTO.setCreatedById(SpringSecurityUtil.getCurrentUserId());
        return toDTO(suggestionRepository.save(toEntity(suggestionDTO)));
    }

    /// Get all
    public List<SuggestionMapper> getAll() {
        checkAdminAccess();
        return suggestionRepository.findAllMapper();
    }

    ///  Get By id
    public SuggestionMapper getById(Integer id) {
        checkAdminAccess();
        return suggestionRepository.findByIdMapper(id).get();
    }

    public List<SuggestionMapper> findByCreatedBy() {
        return suggestionRepository.findByCreatedByMapper(SpringSecurityUtil.getCurrentUserId());
    }


    public Boolean updateSuggestion(Integer id, SuggestionDTO suggestionDTO) {
        checkAdminAccess();
        int effectedRow = suggestionRepository.updateSuggestion(id, suggestionDTO.getTitle(), suggestionDTO.getDescription(),
                suggestionDTO.getCreatedById(), suggestionDTO.getAssignedToId(), LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean deleteWipe(Integer id, Boolean visible) {
        Optional<SuggestionEntity> suggestion = suggestionRepository.findById(id);
        if (suggestion.isEmpty()) {
            throw  new AppBadRequestExeption("Suggestion not found id: " + id);
        }
        if (!suggestion.get().getCreatedBy().getId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
        int effectedRow = suggestionRepository.updateVisible(id, visible, LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean deleteById(Integer id) {
        checkAdminAccess();
        suggestionRepository.deleteById(id);
        return true;
    }

    // Entity to DTO conversion
    public SuggestionDTO toDTO(SuggestionEntity entity) {
        if (entity == null) {return null;}
        SuggestionDTO dto = new SuggestionDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setVisible(entity.getVisible());
        if (entity.getCreatedBy() != null) {dto.setCreatedById(entity.getCreatedBy().getId());}
        if (entity.getAssignedTo() != null) {dto.setAssignedToId(entity.getAssignedTo().getId());}
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }

    // DTO to Entity conversion
    public SuggestionEntity toEntity(SuggestionDTO dto) {
        if (dto == null) {return null;}
        SuggestionEntity entity = new SuggestionEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setVisible(dto.getVisible());
        if (dto.getCreatedById() != null) {entity.setCreatedBy(employeeService.getByIdEntity(dto.getCreatedById()));}
        if (dto.getAssignedToId() != null) {entity.setAssignedTo(departmentService.getByIdEntity(dto.getAssignedToId()));}
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        return entity;
    }

    ///  Get By Id Entity
    public SuggestionEntity getByIdEntity(Integer id) {
        Optional<SuggestionEntity> suggestion = suggestionRepository.findById(id);
        if (suggestion.isEmpty()) {return null;}
        return suggestion.get();
    }

    public PageImpl<SuggestionMapper> pagination(int page, int size){
        checkAdminAccess();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SuggestionMapper> pageObj = suggestionRepository.findAllPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        long total = pageObj.getTotalElements();
        return new PageImpl<SuggestionMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<SuggestionDTO> filter(SuggestionFilterDTO dto, int page, int size) {
        checkAdminAccess();
        PageImpl<SuggestionEntity> result = suggestionCustomRepository.filter(dto, page, size);
        List<SuggestionDTO> dtoResult = new LinkedList<>();
        for (SuggestionEntity entity : result){dtoResult.add(toDTO(entity));}
        return new PageImpl<>(dtoResult, PageRequest.of(page, size), result.getTotalElements());
    }

    private void checkAdminAccess() {
        if (!SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
    }
}
