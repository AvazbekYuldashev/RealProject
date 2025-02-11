package com.example.department_management_system.service;

import com.example.department_management_system.dto.CompletedWorkDTO;
import com.example.department_management_system.dto.CompletedWorkFilterDTO;
import com.example.department_management_system.entity.CompletedWorkEntity;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.CompletedWorkMapper;
import com.example.department_management_system.repository.CompletedWorkCustomRepository;
import com.example.department_management_system.repository.CompletedWorkRepository;
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
public class CompletedWorkService {
    @Autowired
    @Lazy
    private CompletedWorkRepository completedWorkRepository;
    @Autowired
    @Lazy
    private ApplicationService applicationService;
    @Autowired
    @Lazy
    private DepartmentService departmentService;
    @Autowired
    @Lazy
    private EmployeeService employeeService;
    @Autowired
    private CompletedWorkCustomRepository completedWorkCustomRepository;

    ///  Create CompletedWork  --> (Yangi bajarilgan ish yaratish).
    public CompletedWorkDTO saveCompletedWork(CompletedWorkDTO completedWorkDTO) {
        completedWorkDTO.setCreatedDate(LocalDateTime.now());
        completedWorkDTO.setUpdatedDate(LocalDateTime.now());
        completedWorkDTO.setVisible(true);
        completedWorkDTO.setEmployeeId(SpringSecurityUtil.getCurrentUserId());
        if (SpringSecurityUtil.getCurrentDepartmentId() == null){
            throw new AppBadRequestExeption("This user department not found.");
        }
        completedWorkDTO.setDepartmentId(SpringSecurityUtil.getCurrentDepartmentId());
        return toDTO(completedWorkRepository.save(toEntity(completedWorkDTO)));
    }

    /// Get All --> (Barcha bajarilgan ishlarni olish).
    public List<CompletedWorkMapper> getAll(){
        return completedWorkRepository.findAllMapper();
    }

    ///  Get by Id  --> (ID bo‘yicha bajarilgan ishni qaytarish).
    public CompletedWorkMapper getById(Integer id) {
        return completedWorkRepository.findByIdMapper(id).get();
    }

    /// Get by Application --> (Ariza Id boyicha Barcha bajarilgan ishlarni olish)
    public List<CompletedWorkMapper> findByApplication(Integer applicationId) {
        return completedWorkRepository.findByApplicationMapper(applicationId);
    }

    /// Get by Department --> (Bo'lim Id boyicha Barcha bajarilgan ishlarni olish)
    public List<CompletedWorkMapper> findByDepartmentId(Integer departmentId) {
        return completedWorkRepository.findByDepartmentIdMapper(departmentId);
    }

    /// Get by Employee --> (Xodim Id boyicha Barcha bajarilgan ishlarni olish)
    public List<CompletedWorkMapper> findByEmployeeId(Integer employeeId) {
        return completedWorkRepository.findByEmployeeIdMapper(employeeId);
    }

    /// Update Employee --> (Bajargan xodimni yangilash).
    public Boolean updateEmployee(Integer id, CompletedWorkDTO completedWorkDTO) {
        int effectedRow =  completedWorkRepository.updateEmployee(id, completedWorkDTO.getEmployeeId(), LocalDateTime.now());
        return effectedRow > 0;
    }

    /// Update Complated Work --> (Toliq yangilash).
    public Boolean updateCompletedWork(Integer id, CompletedWorkDTO completedWorkDTO) {
        int effectedRow =  completedWorkRepository.updateCompletedWork(id, completedWorkDTO.getApplicationId(),
                completedWorkDTO.getDepartmentId(), completedWorkDTO.getEmployeeId(), LocalDateTime.now());
        return effectedRow > 0;
    }

    /// Update Visible --> (Ko‘rinish holatini (visible) yangilash.)
    public Boolean deleteWipe(Integer id, Boolean visible) {
        int effectedRow = completedWorkRepository.updateVisible(id, visible, LocalDateTime.now());
        return effectedRow > 0;
    }

    /// Delete By Id --> (ID bo‘yicha Bajarilgan ishni o‘chirish.)
    public Boolean deleteById(Integer id) {
        completedWorkRepository.deleteById(id);
        return true;
    }

    public PageImpl<CompletedWorkMapper> pagination(int page, int size){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CompletedWorkMapper> pageObj = completedWorkRepository.findAllPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        long total = pageObj.getTotalElements();
        return new PageImpl<CompletedWorkMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<CompletedWorkDTO> filter(CompletedWorkFilterDTO dto, int page, int size) {
        PageImpl<CompletedWorkEntity> result = completedWorkCustomRepository.filter(dto, page, size);
        List<CompletedWorkDTO> dtoResult = new LinkedList<>();
        for (CompletedWorkEntity entity : result){dtoResult.add(toDTO(entity));}
        return new PageImpl<>(dtoResult, PageRequest.of(page, size), result.getTotalElements());
    }

    ///  ToDTO --> (Entity'ni DTO'ga o‘tkazish).
    public CompletedWorkDTO toDTO(CompletedWorkEntity entity) {
        if (entity == null) {return null;}
        CompletedWorkDTO dto = new CompletedWorkDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        if (entity.getApplication() != null) {dto.setApplicationId(entity.getApplication().getId());}
        if (entity.getDepartment() != null) {dto.setDepartmentId(entity.getDepartment().getId());}
        if (entity.getEmployee() != null) {dto.setEmployeeId(entity.getEmployee().getId());}
        return dto;
    }

    ///  ToEntity --> (DTO'ni Entity'ga o‘tkazish).
    public CompletedWorkEntity toEntity(CompletedWorkDTO dto) {
        if (dto == null) {return null;}
        CompletedWorkEntity entity = new CompletedWorkEntity();
        entity.setId(dto.getId());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setVisible(dto.getVisible());
        if (dto.getApplicationId() != null) {entity.setApplication(applicationService.getByIdEntity(dto.getApplicationId()));}
        if (dto.getDepartmentId() != null) {entity.setDepartment(departmentService.getByIdEntity(dto.getDepartmentId()));}
        if (dto.getEmployeeId() != null) {entity.setEmployee(employeeService.getByIdEntity(dto.getEmployeeId()));}
        return entity;
    }

    ///  Get by Id  --> (ID bo‘yicha bajarilgan ishni qaytarish).
    public CompletedWorkEntity getByIdEntity(Integer id) {
        Optional<CompletedWorkEntity> completedWorkEntity = completedWorkRepository.findById(id);
        if(completedWorkEntity.isEmpty()){
            return null;
        }
        return completedWorkEntity.get();
    }
}
