package com.example.department_management_system.service;

import com.example.department_management_system.dto.EmployeeDTO;
import com.example.department_management_system.dto.EmployeeFilterDTO;
import com.example.department_management_system.dto.EmployeeUpdateDTO;
import com.example.department_management_system.entity.DepartmentEntity;
import com.example.department_management_system.entity.EmployeeEntity;
import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.enums.EmployeeStatus;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.mapper.EmployeeMapper;
import com.example.department_management_system.repository.DepartmentRepository;
import com.example.department_management_system.repository.EmployeeCustomRepository;
import com.example.department_management_system.repository.EmployeeRepository;
import com.example.department_management_system.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    @Lazy
    private EmployeeRepository employeeRepository;
    @Autowired
    @Lazy
    private DepartmentService departmentService;
    @Autowired
    private EmployeeCustomRepository employeeCustomRepository;
    @Autowired
    private BCryptPasswordEncoder bc;
    @Autowired
    private DepartmentRepository departmentRepository;

    public EmployeeEntity getByIdEntity(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new AppBadRequestExeption("Employee not found with id: " + id));
    }


    ///  Create Employee
    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        checkAdminAccess();
        emailValidC(dto.getEmail());
        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setRole(dto.getRole());
        entity.setPosition(dto.getPosition());
        entity.setStatus(EmployeeStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setEmail(dto.getEmail());
        entity.setPassword(bc.encode(dto.getPassword()));
        employeeRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setStatus(entity.getStatus());
        dto.setVisible(entity.getVisible());
        dto.setPassword("");
        return dto;
    }
    ///  Get All Employee
    public List<EmployeeMapper> getAll() {
        checkAdminAccess();
        return employeeRepository.findAllMapper();
    }

    ///  Get Employee By Id
    public EmployeeMapper getById(Integer id) {
        checkAdminAccess();
        return employeeRepository.findByIdMapper(id).orElseThrow(() ->
                new AppBadRequestExeption("Employee not found with id: " + id)
        );
    }

    public EmployeeMapper getMe() {
        return employeeRepository.findByIdMapper(SpringSecurityUtil.getCurrentUserId()).orElseThrow(() ->
                new AppBadRequestExeption("Employee not found with id: " + SpringSecurityUtil.getCurrentUserId())
        );
    }

    /// berilgan bolimdagi xodimlar royxati
    public List<EmployeeMapper> getByDepartmentId(Integer departmentId) {
        Integer currentDepartmentId = SpringSecurityUtil.getCurrentDepartmentId();
        if (SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN) || currentDepartmentId != null && currentDepartmentId.equals(departmentId)) {
            return employeeRepository.findByDepartmentMapper(departmentId);
        } else if(currentDepartmentId == null) {
            throw new AppBadRequestExeption("Current Department ID is null");
        }
        throw new AppBadRequestExeption("It does not belong to the current profile.");
    }

    @Transactional
    public Boolean updateRole(Integer id, EmployeeDTO employeeDTO) {
        checkAdminAccess();
        EmployeeEntity employee = getByIdEntity(id);
        if (employee == null) {
            throw new AppBadRequestExeption("Employee ID not found: " + id);
        }
        if (id.equals(1)){
            throw new AppBadRequestExeption("Employee ID is unique ADMIN");
        }
        int effectedRow = employeeRepository.updateRole(id, employeeDTO.getRole(), LocalDateTime.now());
        return effectedRow > 0;
    }

    @Transactional
    public Boolean updatePosition(Integer id, EmployeeDTO employeeDTO) {
        checkAdminAccess();
        EmployeeEntity employee = getByIdEntity(id);
        if (employee == null) {
            throw new AppBadRequestExeption("Employee ID not found: " + id);
        }
        int effectedRow = employeeRepository.updatePosition(id, employeeDTO.getPosition(), LocalDateTime.now());
        return effectedRow > 0;
    }

    @Transactional
    public Boolean updateStatus(Integer id, EmployeeDTO employeeDTO) {
        checkAdminAccess();
        EmployeeEntity employee = getByIdEntity(id);
        if (employee == null) {
            throw new AppBadRequestExeption("Employee ID not found: " + id);
        }
        int effectedRow = employeeRepository.updateStatus(id, employeeDTO.getStatus(), LocalDateTime.now());
        return effectedRow > 0;
    }

    @Transactional
    public Boolean updateDepartment(Integer id, EmployeeDTO employeeDTO) {
        checkAdminAccess();
        EmployeeEntity employee = getByIdEntity(id);
        if (employee == null) {
            throw new AppBadRequestExeption("Employee ID not found: " + id);
        }
        Optional<DepartmentEntity> department = departmentRepository.findByIdCustom(employeeDTO.getDepartmentId());
        if (department.isEmpty()){
            throw new AppBadRequestExeption("Department ID not found");
        }
        int effectedRow = employeeRepository.updateDepartment(id, employeeDTO.getDepartmentId(), LocalDateTime.now());
        return effectedRow > 0;
    }
    ///  Update
    @Transactional
    public Boolean updateEmployee(Integer id, EmployeeUpdateDTO employeeUpdateDTO) {
        isValid(employeeUpdateDTO);
        EmployeeEntity employee = getByIdEntity(id);
        if (employee == null) {throw new AppBadRequestExeption("Employee ID not found: " + id);}
        if (id.equals(1)){throw new AppBadRequestExeption("Employee ID is unique ADMIN");}
        if (employeeUpdateDTO.getEmail() != null){emailValid(employeeUpdateDTO.getEmail(), id);}
        if (employeeUpdateDTO.getPassword() != null && !employeeUpdateDTO.getPassword().isEmpty()) {
            employeeUpdateDTO.setPassword(bc.encode(employeeUpdateDTO.getPassword()));
        }
        if (SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            return updateAdmin(id, employeeUpdateDTO);
        }
        if (SpringSecurityUtil.getCurrentUserId().equals(id)){
            return updateUser(id, employeeUpdateDTO);
        }
        throw new AppBadRequestExeption("It does not belong to the current profile.");
    }

    @Transactional
    public Boolean deleteWipeA(Integer id, Boolean visible) {
        checkAdminAccess();
        int effectedRow = employeeRepository.updateVisible(id, visible, LocalDateTime.now());
        return effectedRow > 0;
    }

    @Transactional
    public Boolean deleteWipe() {
        if (SpringSecurityUtil.getCurrentUserId().equals(1)){
            throw new AppBadRequestExeption("This User Uniq Admin");
        }
        int effectedRow = employeeRepository.updateVisible(SpringSecurityUtil.getCurrentUserId(), false, LocalDateTime.now());
        return effectedRow > 0;
    }

    @Transactional
    public Boolean deleteById(Integer id) {
        checkAdminAccess();
        EmployeeEntity employee = getByIdEntity(id);
        if (employee != null && id != 1) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public PageImpl<EmployeeMapper> pagination(int page, int size){
        checkAdminAccess();
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeMapper> pageObj = employeeRepository.findAllPageble(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        long total = pageObj.getTotalElements();
        return new PageImpl<EmployeeMapper>(pageObj.getContent(), pageable, total);
    }

    public PageImpl<EmployeeDTO> filter(EmployeeFilterDTO dto, int page, int size) {
        checkAdminAccess();
        PageImpl<EmployeeEntity> result = employeeCustomRepository.filter(dto, page, size);
        List<EmployeeDTO> dtoResult = new LinkedList<>();
        for (EmployeeEntity entity : result) {
            dtoResult.add(toDto(entity));
        }
        return new PageImpl<>(dtoResult, PageRequest.of(page, size), result.getTotalElements());
    }










    public EmployeeDTO toDto(EmployeeEntity entity) {
        if (entity == null) {return null;}
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
        dto.setPosition(entity.getPosition());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        dto.setEmail(entity.getEmail());
        if (entity.getDepartment() != null) {dto.setDepartmentId(entity.getDepartment().getId());}
        return dto;
    }

    // Method to convert EmployeeDTO to EmployeeEntity
    public EmployeeEntity toEntity(EmployeeDTO dto) {
        if (dto == null) {return null;}
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setRole(dto.getRole());
        entity.setPosition(dto.getPosition());
        entity.setStatus(dto.getStatus());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setVisible(dto.getVisible());
        if (dto.getDepartmentId() != null) {entity.setDepartment(departmentService.getByIdEntity(dto.getDepartmentId()));}
        return entity;
    }

    private Boolean updateUser(Integer id, EmployeeUpdateDTO employeeUpdateDTO) {
        int effectedRow = employeeRepository.updateCurrentUser(id, employeeUpdateDTO.getName(), employeeUpdateDTO.getSurname(),
                employeeUpdateDTO.getEmail(), employeeUpdateDTO.getPassword(), LocalDateTime.now());
        return effectedRow > 0;
    }

    private Boolean updateAdmin(Integer id, EmployeeUpdateDTO employeeUpdateDTO){
        int effectedRow = employeeRepository.updateEmployee(id, employeeUpdateDTO.getName(),
                employeeUpdateDTO.getSurname(), employeeUpdateDTO.getRole(), employeeUpdateDTO.getPosition(),
                employeeUpdateDTO.getStatus(), employeeUpdateDTO.getDepartmentId(),
                employeeUpdateDTO.getEmail(), employeeUpdateDTO.getPassword(), LocalDateTime.now());
        return effectedRow > 0;
    }

    private void checkAdminAccess() {
        if (!SpringSecurityUtil.getCurrentEmployeeRole().equals(EmployeeRole.ADMIN.toString())) {
            throw new AppBadRequestExeption("It does not belong to the current profile.");
        }
    }

    private Boolean emailValid(String email, Integer id) {
        Optional<EmployeeEntity> entity = employeeRepository.findByEmailAndVisibleTrue(email);
        if (entity.isPresent()) { // Agar email bazada bo'lsa
            if (!entity.get().getId().equals(id)) { // Email boshqa odamniki bo'lsa
                throw new AppBadRequestExeption("This email is busy.");
            }
        }
        return true; // Agar email mavjud bo'lsa va aynan shu userniki bo'lsa yoki umuman yo'q bo'lsa, true qaytaradi
    }

    private Boolean emailValidC(String email) {
        Optional<EmployeeEntity> entity = employeeRepository.findByEmailAndVisibleTrue(email);
        if (entity.isPresent()) { // Agar email bazada bo'lsa
                throw new AppBadRequestExeption("This email is busy.");
        }
        return true; // Agar email mavjud bo'lsa va aynan shu userniki bo'lsa yoki umuman yo'q bo'lsa, true qaytaradi
    }

    private void isValid(EmployeeUpdateDTO employeeUpdateDTO) {
        if (employeeUpdateDTO == null) {
            throw new AppBadRequestExeption("The employee must not be null.");
        }
        if (employeeUpdateDTO.getEmail() != null && employeeUpdateDTO.getEmail().trim().isEmpty()) {
            throw new AppBadRequestExeption("The email must not be blank.");
        }
        if (employeeUpdateDTO.getPassword() != null && employeeUpdateDTO.getPassword().trim().isEmpty()) {
            throw new AppBadRequestExeption("The password must not be blank.");
        }
        if (employeeUpdateDTO.getName() != null && employeeUpdateDTO.getName().trim().isEmpty()) {
            throw new AppBadRequestExeption("The name must not be blank.");
        }
        if (employeeUpdateDTO.getSurname() != null && employeeUpdateDTO.getSurname().trim().isEmpty()) {
            throw new AppBadRequestExeption("The surname must not be blank.");
        }
    }



}
