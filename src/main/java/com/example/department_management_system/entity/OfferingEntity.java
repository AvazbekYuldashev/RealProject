package com.example.department_management_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


/// Xizmatlar
@Entity
@Table(name = "offering")
public class OfferingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                     /// Id
    @Column(name = "status", nullable = false)
    private Boolean status = true ;                         /// Xolati (Ishlayabdi toxtatilgan)
    @Column(name = "title", nullable = false)
    private String title;                                   /// Nomi
    @Column(name = "description", nullable = false)
    private String description;                             /// Tavsifi
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;                      /// Yaratilgan sanasi
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;                      /// O'zgartirilgan sanasi
    @OneToMany(mappedBy = "offering")
    private List<ApplicationEntity> applications;           /// Xizmatga bog'langan arizalar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;                    /// Xizmatga bog'langan bo'lim (department)
    @Column(name = "visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible;                /// korinishi


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

    public List<ApplicationEntity> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationEntity> applications) {
        this.applications = applications;
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
