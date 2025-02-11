package com.example.department_management_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

///  Bajarilgan ishlar
@Entity
@Table(name = "completed_work")
public class CompletedWorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     /// ID
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;      /// Yaratilgan sanasi
    @Column(name = "updated_date")          ///  Yangilangan sanasi
    private LocalDateTime updatedDate;
    @OneToOne(fetch = FetchType.LAZY)       /// Bajarilgan ishga tegishli ariza
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)     /// Bajarilgan ishga tegishli bo'lim
    private DepartmentEntity department;    /// Bo'lim
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)       /// Xizmat ko'rsatgan xodim
    private EmployeeEntity employee;        /// Xodim
    @Column(name = "visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible;                /// korinishi

    public CompletedWorkEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
