package com.example.department_management_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

///  Bo'limga kelgan takliflar
@Entity
@Table(name = "suggestion")
public class SuggestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                             /// ID
    @Column(name = "title", nullable = false)
    private String title;                                           /// Title
    @Column(name = "description", length = 1000, nullable = false)
    private String description;                                     /// Description
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private EmployeeEntity createdBy;                               /// Kim yaratgani (Xodim)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false)
    private DepartmentEntity assignedTo;                            /// Qabul qilgan Bo'lim
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();        /// Yaratilgan sanasi
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();        /// Yangilangan sanasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;
    @Column(name = "visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible;                /// korinishi

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public EmployeeEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EmployeeEntity createdBy) {
        this.createdBy = createdBy;
    }

    public DepartmentEntity getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(DepartmentEntity assignedTo) {
        this.assignedTo = assignedTo;
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
