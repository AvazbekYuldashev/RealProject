package com.example.department_management_system.mapper;

import java.time.LocalDateTime;

public interface SuggestionMapper {
    Integer getId();                                      /// ID
    String getTitle();                                    /// Taklif nomi
    String getDescription();                              /// Taklif tavsifi
    Integer getCreatedById();                             /// Taklifni yaratgan xodim IDsi
    Integer getAssignedToId();                            /// Taklifni qabul qilgan bo'lim IDsi
    LocalDateTime getCreatedDate();                       /// Yaratilgan sanasi
    LocalDateTime getUpdatedDate();                       /// Yangilangan sanasi
}
