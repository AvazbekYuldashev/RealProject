package com.example.department_management_system.service;

import com.example.department_management_system.dto.ApplicationDTO;
import com.example.department_management_system.dto.ApplicationFilterDTO;
import com.example.department_management_system.entity.ApplicationEntity;
import com.example.department_management_system.enums.ApplicationStatus;
import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.ApplicationMapper;
import com.example.department_management_system.repository.ApplicationCustomRepository;
import com.example.department_management_system.repository.ApplicationRepository;
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
public class ApplicationService {
    @Autowired
    @Lazy
    private ApplicationRepository applicationRepository;
    @Autowired
    @Lazy
    private OfferingService offeringService;
    @Autowired
    @Lazy
    private EmployeeService employeeService;
    @Autowired
    @Lazy
    private DepartmentService departmentService;
    @Autowired
    @Lazy
    private CompletedWorkService completedWorkService;
    @Autowired
    private ApplicationCustomRepository applicationCustomRepository;

    ///  Create application  --> (Yangi ariza yaratish).
    public ApplicationDTO saveApplication(ApplicationDTO applicationDTO) {
        applicationDTO.setCreatedById(SpringSecurityUtil.getCurrentUserId());
        applicationDTO.setCreatedDate(LocalDateTime.now());
        applicationDTO.setUpdatedDate(LocalDateTime.now());
        applicationDTO.setVisible(true);
        applicationDTO.setStatus(ApplicationStatus.SENT);
        ApplicationEntity applicationEntity = toEntity(applicationDTO);
        return toDTO(applicationRepository.save(applicationEntity));
    }

    /// Get All --> (Barcha arizalarni olish)
    public List<ApplicationMapper> getAll() {
        checkAdminAccess();
        return applicationRepository.findAllMapper();
    }

    ///  Get by Id  --> (ID bo‘yicha ApplicationMapperni qaytarish).
    public ApplicationMapper getById(Integer id) {
        checkAdminAccess();
        Optional<ApplicationMapper> applicationMapper = applicationRepository.findByIdMapper(id);
        if (applicationMapper.isEmpty()) {
            return null;
        }
        return applicationMapper.get();
    }

    /// Get By CreatedBy --> (Arizani yuborgan xodim bo‘yicha qidirish).
    public List<ApplicationMapper> findByCreatedBy() {
        return applicationRepository.findByCreatedByMapper(SpringSecurityUtil.getCurrentUserId());

    }

    /// Get By AssingedTo --> (Arizani bajargan xodim bo‘yicha qidirish).
    public List<ApplicationMapper> findByAssignedTo() {
        checkAdminAccess();
        return applicationRepository.findByAssignedToMapper(SpringSecurityUtil.getCurrentUserId());
    }

    /// Get By Department -->  (Ariza yuborilgan bo‘lim bo‘yicha qidirish).
    public List<ApplicationMapper> findByDepartmentId(Integer id) {
        Integer currentDepartmentId = SpringSecurityUtil.getCurrentDepartmentId();
        if (currentDepartmentId != null && currentDepartmentId.equals(id)) {
            checkAdminAccess();
        }
        return applicationRepository.findByDepartmentMapper(id);
    }

    /// Update AssingedTo --> (Bajargan xodimni yangilash).
    public Boolean updateAssignedTo(Integer id, ApplicationDTO applicationDTO) {
        checkAdminAccess();
        int effectedRow = applicationRepository.updateAssignedTo(id, applicationDTO.getAssignedToId(), ApplicationStatus.APPROVED, LocalDateTime.now());
        return effectedRow > 0;
    }

    ///  Get By Status --> (Ariza holati bo‘yicha qidirish).
    public List<ApplicationMapper> findByStatus(ApplicationStatus status){
        checkAdminAccess();
        if (!(status.equals(ApplicationStatus.SENT) ||
                status.equals(ApplicationStatus.APPROVED) ||
                status.equals(ApplicationStatus.IN_PROGRESS) ||
                status.equals(ApplicationStatus.COMPLETED))) {
            throw new IllegalArgumentException("Invalid application status: " + status);
        }
        return applicationRepository.findByStatusMapper(status);
    }

    /// Update --> (Holatni yangilash).
    public Boolean updateStatus(Integer id, ApplicationDTO applicationDTO) {
        ApplicationEntity application = getByIdEntity(id);
        if (!application.getAssignedTo().getId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AppBadRequestExeption("This application cannot be modified by the user who submitted it.");
        }
        ApplicationStatus status = applicationDTO.getStatus();
        if (!(status.equals(ApplicationStatus.SENT) ||
                status.equals(ApplicationStatus.APPROVED) ||
                status.equals(ApplicationStatus.IN_PROGRESS) ||
                status.equals(ApplicationStatus.COMPLETED))) {
            throw new IllegalArgumentException("Invalid application status: " + status);
        }
        int effectedRow =  applicationRepository.updateStatus(id, applicationDTO.getStatus(), LocalDateTime.now());
        return effectedRow > 0;
    }

    /// Update Application --> (To‘liq arizani yangilash).
    public Boolean updateApplication(Integer id, ApplicationDTO applicationDTO) {
        ApplicationEntity application = getByIdEntity(id);
        if (application.getCreatedBy().getId().equals(SpringSecurityUtil.getCurrentUserId()) && application.getStatus() != ApplicationStatus.SENT){
            int effectedRow =  applicationRepository.updateTitleAndDescription(id, applicationDTO.getTitle(), applicationDTO.getDescription(), LocalDateTime.now());
            return effectedRow > 0;
        } else {
            checkAdminAccess();
            int effectedRow = applicationRepository.updateApplication(id, applicationDTO.getTitle(), applicationDTO.getDescription(),
                    applicationDTO.getOfferingId(), applicationDTO.getCreatedById(), applicationDTO.getAssignedToId(),
                    applicationDTO.getDepartmentId(), applicationDTO.getStatus(), applicationDTO.getCompletedWorkId(), LocalDateTime.now());
            return effectedRow > 0;
        }
    }

    /// Update Visible --> (Ko‘rinish holatini (visible) yangilash.)
    public Boolean updateVisible(Integer id, Boolean visible) {
        ApplicationEntity application = getByIdEntity(id);
        if (application.getCreatedBy().getId().equals(SpringSecurityUtil.getCurrentUserId())){
            int effectedRow = applicationRepository.updateVisible(id, visible, LocalDateTime.now());
            return effectedRow > 0;
        }
        throw new AppBadRequestExeption("It does not belong to the current profile.");
    }

    /// Delete By Id --> (ID bo‘yicha arizani o‘chirish.)
    public Boolean deleteById(Integer id) {
        checkAdminAccess();
        ApplicationEntity application = getByIdEntity(id);
        applicationRepository.deleteById(id);
        return true;
    }

    ///  ToDTO --> (Entity'ni DTO'ga o‘tkazish).
    public ApplicationDTO toDTO(ApplicationEntity entity) {
        if (entity == null) {return null;}
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        dto.setStatus(entity.getStatus());
        if (entity.getOffering() != null){dto.setOfferingId(entity.getOffering().getId());}
        if (entity.getCreatedBy() != null){dto.setCreatedById(entity.getCreatedBy().getId());}
        if (entity.getAssignedTo() != null){dto.setAssignedToId(entity.getAssignedTo().getId());}
        if (entity.getDepartment() != null){dto.setDepartmentId(entity.getDepartment().getId());}
        if (entity.getCompletedWork() != null) {dto.setCompletedWorkId(entity.getCompletedWork().getId());}
        return dto;
    }

    ///  ToEntity --> (DTO'ni Entity'ga o‘tkazish).
    public ApplicationEntity toEntity(ApplicationDTO dto) {
        if (dto == null) {return null;}
        ApplicationEntity entity = new ApplicationEntity();
        entity.setVisible(dto.getVisible());
        if (dto.getId() != null) {entity.setId(dto.getId());}
        if (dto.getTitle() != null) {entity.setTitle(dto.getTitle());}
        if (dto.getDescription() != null) {entity.setDescription(dto.getDescription());}
        if (dto.getCreatedDate() != null) {entity.setCreatedDate(dto.getCreatedDate());}
        if (dto.getUpdatedDate() != null) {entity.setUpdatedDate(dto.getUpdatedDate());}
        if (dto.getStatus() != null) {entity.setStatus(dto.getStatus());}
        if (dto.getOfferingId() != null) {entity.setOffering(offeringService.getByIdEntity(dto.getOfferingId()));}
        if (dto.getCreatedById() != null) {entity.setCreatedBy(employeeService.getByIdEntity(dto.getCreatedById()));}
        if (dto.getAssignedToId() != null) {entity.setAssignedTo(employeeService.getByIdEntity(dto.getAssignedToId()));}
        if (dto.getDepartmentId() != null) {entity.setDepartment(departmentService.getByIdEntity(dto.getDepartmentId()));}
        if (dto.getCompletedWorkId() != null) {entity.setCompletedWork(completedWorkService.getByIdEntity(dto.getCompletedWorkId()));}
        return entity;
    }

    ///  Get by Id Entity --> (ID bo‘yicha ApplicationEntityni qaytarish)
    public ApplicationEntity getByIdEntity(Integer id){
        Optional<ApplicationEntity> applicationEntity = applicationRepository.findById(id);
        if (applicationEntity.isEmpty()) {throw new AppBadRequestExeption("Application does not exist.");}
        return applicationEntity.get();
    }

    public PageImpl<ApplicationMapper> pagination(int page, int size){
        checkAdminAccess();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ApplicationMapper> pageObj = applicationRepository.findAllPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        long total = pageObj.getTotalElements();
        return new PageImpl<ApplicationMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<ApplicationDTO> filter(ApplicationFilterDTO dto, int page, int size) {
        if (!(dto.getStatus().equals(ApplicationStatus.SENT) ||
                dto.getStatus().equals(ApplicationStatus.APPROVED) ||
                dto.getStatus().equals(ApplicationStatus.IN_PROGRESS) ||
                dto.getStatus().equals(ApplicationStatus.COMPLETED))) {
            throw new IllegalArgumentException("Invalid application status: " + dto.getStatus());
        }
        PageImpl<ApplicationEntity> result = applicationCustomRepository.filter(dto, page, size);
        List<ApplicationDTO> dtoResult = new LinkedList<>();
        for (ApplicationEntity entity : result){
            System.out.println(entity.toString());
            dtoResult.add(toDTO(entity));}
        return new PageImpl<>(dtoResult, PageRequest.of(page, size), result.getTotalElements());
    }

    private void checkAdminAccess() {
        if (!SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
    }

    public PageImpl<ApplicationMapper> getCreatedByPaged(int page, int size){
        Integer id = SpringSecurityUtil.getCurrentUserId();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ApplicationMapper> pageObj = applicationRepository.findCreatedByPaged(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), id);
        long total = pageObj.getTotalElements();
        return new PageImpl<ApplicationMapper>(pageObj.getContent(), pageable, total);
    }
}
