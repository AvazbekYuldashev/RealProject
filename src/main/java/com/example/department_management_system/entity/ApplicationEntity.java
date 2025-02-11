package com.example.department_management_system.entity;

import com.example.department_management_system.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/// Arizalar
@Entity
@Table(name = "application")
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     /// ID
    @Column(name = "title", nullable = false)
    private String title;                   /// Ariza turi
    @Column(name = "description", nullable = false)
    private String description;             /// Ariza matni
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;      /// Ariza yaratilgan sanasi
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;      /// Ariza o'zgartirilgan sanasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    private OfferingEntity offering;        /// Ariza yuborilayotgan Xizmat ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private EmployeeEntity createdBy;       /// Arizani yuborgan xodim
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private EmployeeEntity assignedTo;      /// Arizani ko'rib chiqadigan xodim
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;    /// Ariza yuborilayotgan Bo'lim manzili
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;       /// Ariza xolati
    @OneToOne(mappedBy = "application")
    private CompletedWorkEntity completedWork;  /// Ariza bajarilganligi xaqida malumot
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

    public OfferingEntity getOffering() {
        return offering;
    }

    public void setOffering(OfferingEntity offering) {
        this.offering = offering;
    }

    public EmployeeEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EmployeeEntity createdBy) {
        this.createdBy = createdBy;
    }

    public EmployeeEntity getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(EmployeeEntity assignedTo) {
        this.assignedTo = assignedTo;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public CompletedWorkEntity getCompletedWork() {
        return completedWork;
    }

    public void setCompletedWork(CompletedWorkEntity completedWork) {
        this.completedWork = completedWork;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}