package com.example.department_management_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SuggestionFilterDTO {
    private Integer id;                                      /// ID
    private String title;                                    /// Taklif nomi
    private String description;                              /// Taklif tavsifi
    private Integer createdById;                             /// Taklifni yaratgan xodim IDsi
    private Integer assignedToId;                            /// Taklifni qabul qilgan bo'lim IDsi
    private LocalDate createdDateFrom;                       /// Yaratilgan sanasi
    private LocalDate createdDateTo;                         /// Yaratilgan sanasi
    private LocalDate updatedDateFrom;                       /// Yangilangan sanasi
    private LocalDate updatedDateTo;                         /// Yangilangan sanasi
    private Boolean visible;
    private Integer departmentId;

    public SuggestionFilterDTO(){}

    public SuggestionFilterDTO(Integer id, String title, String description, Integer createdById, Integer assignedToId, LocalDate createdDateFrom, LocalDate createdDateTo, LocalDate updatedDateFrom, LocalDate updatedDateTo, Boolean visible, Integer departmentId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdById = createdById;
        this.assignedToId = assignedToId;
        this.createdDateFrom = createdDateFrom;
        this.createdDateTo = createdDateTo;
        this.updatedDateFrom = updatedDateFrom;
        this.updatedDateTo = updatedDateTo;
        this.visible = visible;
        this.departmentId = departmentId;
    }

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

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Integer getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Integer assignedToId) {
        this.assignedToId = assignedToId;
    }

    public LocalDate getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(LocalDate createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public LocalDate getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(LocalDate createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public LocalDate getUpdatedDateFrom() {
        return updatedDateFrom;
    }

    public void setUpdatedDateFrom(LocalDate updatedDateFrom) {
        this.updatedDateFrom = updatedDateFrom;
    }

    public LocalDate getUpdatedDateTo() {
        return updatedDateTo;
    }

    public void setUpdatedDateTo(LocalDate updatedDateTo) {
        this.updatedDateTo = updatedDateTo;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
