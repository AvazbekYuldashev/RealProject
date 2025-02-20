package com.example.department_management_system.dto;


import com.example.department_management_system.enums.DepartmentStatus;
import com.example.department_management_system.enums.DepartmentType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

///  Bolim
public class DepartmentDTO {
    private Integer id;                                          /// Id
    private DepartmentStatus status;                             /// Xozirgi xolati
    @NotBlank(message = "Title required")
    private String title;                                        /// Bo'lim nomi
    @NotBlank(message = "Description required")
    private String description;                                  /// Bo'lim xaqida qisqacha
    private LocalDateTime createdDate;                           /// Yaratilgan sanasi
    private LocalDateTime updatedDate;                           /// Yangilangan sanasi
    @NotBlank(message = "Address required")
    private String address;                                      /// Bo'lim manzili
    @NotBlank(message = "Phone Number required")
    private String phoneNumber;                                  /// Bo'lim telefon raqami
    private String headOfDepartment;                             /// Bo'lim boshlig'ining ismi yoki ID-si
    private DepartmentType type;                                 /// Bo'lim turi (masalan, "Akademik", "Texnik")
    private List<EmployeeDTO> employeeIds;                       /// Bo'lim tarkibidagi xodimlar IDlari
    private List<OfferingDTO> offeringIds;                       /// Bo'lim tarkibidagi xizmatlar IDlari
    private List<ApplicationDTO> applicationIds;                 /// Bo'limga kelgan arizalar IDlari
    private List<CompletedWorkDTO> completedWorkIds;             /// Bo'limda bajarilgan ishlar IDlari
    private Boolean visible;

    public DepartmentDTO(){}
    public DepartmentDTO(Integer id, DepartmentStatus status, String title, String description,
                         LocalDateTime createdDate, LocalDateTime updatedDate, String address, String phoneNumber,
                         String headOfDepartment, DepartmentType type, List<EmployeeDTO> employees,
                         List<OfferingDTO> offerings, List<ApplicationDTO> applications,
                         List<CompletedWorkDTO> completedWorks) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.headOfDepartment = headOfDepartment;
        this.type = type;
        this.employeeIds = employees;
        this.offeringIds = offerings;
        this.applicationIds = applications;
        this.completedWorkIds = completedWorks;
    }

    /// korinishi


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DepartmentStatus getStatus() {
        return status;
    }

    public void setStatus(DepartmentStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }

    public List<EmployeeDTO> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<EmployeeDTO> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public List<OfferingDTO> getOfferingIds() {
        return offeringIds;
    }

    public void setOfferingIds(List<OfferingDTO> offeringIds) {
        this.offeringIds = offeringIds;
    }

    public List<ApplicationDTO> getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(List<ApplicationDTO> applicationIds) {
        this.applicationIds = applicationIds;
    }

    public List<CompletedWorkDTO> getCompletedWorkIds() {
        return completedWorkIds;
    }

    public void setCompletedWorkIds(List<CompletedWorkDTO> completedWorkIds) {
        this.completedWorkIds = completedWorkIds;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
