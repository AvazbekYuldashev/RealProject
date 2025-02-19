package com.example.department_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

///  Bo'limga kelgan takliflars
public class SuggestionDTO {
    private Integer id;                                      /// ID
    @NotBlank(message = "Title required")
    private String title;                                    /// Taklif nomi
    @NotBlank(message = "Description required")
    private String description;                              /// Taklif tavsifi
    private Integer createdById;                             /// Taklifni yaratgan xodim IDsi
    private Integer assignedToId;                            /// Taklifni qabul qilgan bo'lim IDsi
    private LocalDateTime createdDate;                       /// Yaratilgan sanasi
    private LocalDateTime updatedDate;                       /// Yangilangan sanasi
    private Boolean visible;
    @NotNull(message = "Department Id required")
    private Integer departmentId;

    public SuggestionDTO(){}

    public SuggestionDTO(Integer id, String title, String description, Integer createdById, Integer assignedToId, LocalDateTime createdDate, LocalDateTime updatedDate,  Boolean visible, Integer departmentId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdById = createdById;
        this.assignedToId = assignedToId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.visible = visible;
        this.departmentId = departmentId;
    }

    /// korinishi


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
