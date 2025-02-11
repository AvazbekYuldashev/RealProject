package com.example.department_management_system.service;


import com.example.department_management_system.dto.OfferingDTO;
import com.example.department_management_system.dto.OfferingFilterDTO;
import com.example.department_management_system.entity.DepartmentEntity;
import com.example.department_management_system.entity.OfferingEntity;
import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.OfferingMapper;
import com.example.department_management_system.repository.DepartmentRepository;
import com.example.department_management_system.repository.OfferingCustomRepository;
import com.example.department_management_system.repository.OfferingRepository;
import com.example.department_management_system.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferingService {
    @Autowired
    @Lazy
    private OfferingRepository offeringRepository;
    @Autowired
    @Lazy
    private DepartmentService departmentService;
    @Autowired
    private OfferingCustomRepository offeringCustomRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    /// Create
    public OfferingDTO create(OfferingDTO offeringDTO) {
        checkAdminAccess();
        offeringDTO.setCreatedDate(LocalDateTime.now());
        offeringDTO.setUpdatedDate(LocalDateTime.now());
        offeringDTO.setVisible(true);
        offeringDTO.setStatus(true);
        return toDTO(offeringRepository.save(toEntity(offeringDTO)));
    }

    /// Get All
    public List<OfferingMapper> getAll() {
        return offeringRepository.findAllMapper();
    }

    ///  Get By Id
    public OfferingMapper getById(Integer id) {
        return offeringRepository.findByIdMapper(id).get();
    }

    ///  Get By Department
    public List<OfferingMapper> getByDepartmentId(Integer departmentId) {
        return offeringRepository.findByDepartmentMapper(departmentId);
    }

    public Boolean updateStatus(Integer id, OfferingDTO offeringDTO) {
        checkAdminAccess();
        int effectedRow = offeringRepository.updateStatus(id, offeringDTO.getStatus(), LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean updateDepartmentIdP(Integer id, OfferingDTO offeringDTO) {
        checkAdminAccess();
        Optional<DepartmentEntity> department = departmentRepository.findByIdCustom(offeringDTO.getDepartmentId());
        if (department.isEmpty()){
            throw new AppBadRequestExeption("Department id not found");
        }
        int effectedRow = offeringRepository.updateDepartmentIdP(id, offeringDTO.getDepartmentId(), LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean updateOffering(Integer id, OfferingDTO offeringDTO) {
        checkAdminAccess();
        int effectedRow = offeringRepository.updateOffering(id, offeringDTO.getStatus(),
                offeringDTO.getTitle(), offeringDTO.getDescription(),
                offeringDTO.getDepartmentId(), LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean deleteWipe(Integer id, Boolean visible) {
        checkAdminAccess();
        int effectedRow = offeringRepository.updateVisible(id, visible, LocalDateTime.now());
        return effectedRow > 0;
    }

    public Boolean deleteById(Integer id) {
        checkAdminAccess();
        offeringRepository.deleteById(id);
        return true;
    }

    public PageImpl<OfferingMapper> pagination(int page, int size){
        checkAdminAccess();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OfferingMapper> pageObj = offeringRepository.findAllPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        long total = pageObj.getTotalElements();
        return new PageImpl<OfferingMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<OfferingMapper> getByDepartmentId(int page, int size, Integer departmentId) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OfferingMapper> pageObj = offeringRepository.findAllByDepartmentIdPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), departmentId);
        long total = pageObj.getTotalElements();
        return new PageImpl<OfferingMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<OfferingDTO> filter(OfferingFilterDTO dto, int page, int size) {
        PageImpl<OfferingEntity> result = offeringCustomRepository.filter(dto, page, size);
        List<OfferingDTO> dtoResult = new LinkedList<>();
        for (OfferingEntity entity : result){dtoResult.add(toDTO(entity));}
        return new PageImpl<>(dtoResult, PageRequest.of(page, size), result.getTotalElements());
    }
    public OfferingEntity toEntity(OfferingDTO dto) {
        if (dto == null) {return null;}
        OfferingEntity entity = new OfferingEntity();
        entity.setId(dto.getId());
        entity.setStatus(dto.getStatus());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setVisible(dto.getVisible());
        if (dto.getDepartmentId() != null) {entity.setDepartment(departmentService.getByIdEntity(dto.getDepartmentId()));}
        return entity;
    }

    // Convert OfferingEntity to OfferingDTO
    public OfferingDTO toDTO(OfferingEntity entity) {
        if (entity == null) {return null;}
        OfferingDTO dto = new OfferingDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        if (entity.getDepartment() != null) {dto.setDepartmentId(entity.getDepartment().getId());}
        return dto;
    }

    /// Get By Id
    public OfferingEntity getByIdEntity(Integer id) {
        Optional<OfferingEntity> offeringEntity = offeringRepository.findById(id);
        if (offeringEntity.isEmpty()){return null;}
        return offeringEntity.get();
    }

    private void checkAdminAccess() {
        if (!SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
    }
}
