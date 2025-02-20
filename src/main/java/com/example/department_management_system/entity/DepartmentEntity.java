package com.example.department_management_system.entity;


import com.example.department_management_system.enums.DepartmentStatus;
import com.example.department_management_system.enums.DepartmentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

///  Bolim
@Entity
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                                   /// Id
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DepartmentStatus status;                                      /// Xozirgi xolati
    @Column(name = "title", nullable = false)
    private String title;                                                 /// Bo'lim nomi
    @Column(name = "description", length = 1000, nullable = false)        /// Bo'lim xaqida qisqacha
    private String description;
    @Column(name = "created_date", nullable = false)                      ///  Yaratilgan sanasi
    private LocalDateTime createdDate;
    @Column(name = "updated_date")                                        ///  Yangilangan sanasi
    private LocalDateTime updatedDate;
    @Column(name = "address", nullable = false)                           ///  Bo'lim elektron pochta manzili
    private String address;
    @Column(name = "phone_number")                                        /// Bo'lim telefon raqami
    private String phoneNumber;
    @Column(name = "head_of_department", nullable = false)                /// Bo'lim boshlig'ining ismi yoki ID-si.
    private String headOfDepartment;
    @Column(name = "type", nullable = false)                              /// Bo'lim turi (masalan, "Akademik", "Texnik").
    private DepartmentType type;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;                               /// Bo'lim tarkibidagi xodimlar
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<OfferingEntity> offerings;                               /// Bo'lim tarkibidagi xizmatlar (services)
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<ApplicationEntity> applications;                         /// Bo'limga kelgan arizalar
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<CompletedWorkEntity> completedWorks;                     /// Bo'limda bajarilgan ishalar
    @Column(name = "visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible = true;                                       /// korinishi

    public Integer getId() {return id;}

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

    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
    }

    public List<OfferingEntity> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<OfferingEntity> offerings) {
        this.offerings = offerings;
    }

    public List<ApplicationEntity> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationEntity> applications) {
        this.applications = applications;
    }

    public List<CompletedWorkEntity> getCompletedWorks() {
        return completedWorks;
    }

    public void setCompletedWorks(List<CompletedWorkEntity> completedWorks) {
        this.completedWorks = completedWorks;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
