package com.example.department_management_system.entity;


import com.example.department_management_system.enums.EmployeeRole;
import com.example.department_management_system.enums.EmployeeStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


///  Xodimlar
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                                 /// Id si
    @Column(name = "name", nullable = false)                            /// Ismi
    private String name;
    @Column(name = "surname", nullable = false)                         /// Familyasi
    private String surname;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")                                              ///  Roli
    private EmployeeRole role = EmployeeRole.USER;
    @Column(name = "position")                                          /// Lavozimi
    private String position;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)                          /// Statusi (Blok yoki activ)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;                                  /// Yaratilgan sanasi
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;                                  /// Yangilangan sanasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")                                 /// Boglangan bo'lim
    private DepartmentEntity department;
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<ApplicationEntity> applications;                       /// Xodimga tegishli bo'lgan arizalar
    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private List<ApplicationEntity> assignedApplications;               /// Xodimga tayinlangan arizalar
    @Column(name = "visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible;                                            /// korinishi



    public EmployeeEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
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

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }



    public List<ApplicationEntity> getAssignedApplications() {
        return assignedApplications;
    }

    public void setAssignedApplications(List<ApplicationEntity> assignedApplications) {
        this.assignedApplications = assignedApplications;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
