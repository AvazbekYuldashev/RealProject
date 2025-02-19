package com.example.department_management_system.repository;

import com.example.department_management_system.entity.SuggestionEntity;
import com.example.department_management_system.mapper.SuggestionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<SuggestionEntity, Integer> {

    @Query("SELECT s.id as id, s.title as title, s.description as description, u.id as createdById, " +
            "e.id as assignedTo, s.createdDate as createdDate, s.updatedDate as updatedDate " +
            "FROM SuggestionEntity s LEFT JOIN s.createdBy u LEFT JOIN s.assignedTo e " +
            "WHERE s.visible = true order by s.createdDate desc")
    List<SuggestionMapper> findAllMapper();

    @Query("SELECT s.id as id, s.title as title, s.description as description, u.id as createdById, " +
            "e.id as assignedTo, s.createdDate as createdDate, s.updatedDate as updatedDate " +
            "FROM SuggestionEntity s LEFT JOIN s.createdBy u LEFT JOIN s.assignedTo e " +
            "WHERE s.id = :idP AND s.visible = true order by s.createdDate desc")
    Optional<SuggestionMapper> findByIdMapper(@Param("idP") Integer id);

    @Query("SELECT s.id as id, s.title as title, s.description as description, u.id as createdById, " +
            "e.id as assignedTo, s.createdDate as createdDate, s.updatedDate as updatedDate " +
            "FROM SuggestionEntity s LEFT JOIN s.createdBy u LEFT JOIN s.assignedTo e " +
            "WHERE s.createdBy.id = :createdById AND s.visible = true order by s.createdDate desc")
    List<SuggestionMapper> findByCreatedByMapper(@Param("createdById") Integer createdBy);

    @Modifying
    @Transactional
    @Query("UPDATE SuggestionEntity s " +
            " SET s.title = COALESCE(:titleP, s.title), " +
            "     s.description = COALESCE(:descriptionP, s.description), " +
            "     s.createdBy.id = COALESCE(:createdByIdP, s.createdBy.id), " +
            "     s.assignedTo.id = COALESCE(:assignedToIdP, s.assignedTo.id), " +
            "     s.updatedDate = :updatedDateP " +
            "WHERE s.id = :idP AND s.visible = true")
    int updateSuggestion(
            @Param("idP") Integer id,
            @Param("titleP") String title,
            @Param("descriptionP") String description,
            @Param("createdByIdP") Integer createdById,
            @Param("assignedToIdP") Integer assignedToId,
            @Param("updatedDateP") LocalDateTime updatedDateP);


    @Modifying
    @Transactional
    @Query("UPDATE SuggestionEntity set visible = :visibleP, updatedDate = :updatedDateP where id = :idP")
    int updateVisible(@Param("idP") Integer id, @Param("visibleP") Boolean visible, @Param("updatedDateP") LocalDateTime updatedDateP);

    @Modifying
    @Transactional
    @Query("DELETE SuggestionEntity where id = :idP")
    void deleteById(@Param("idP") Integer id);

    @Query("SELECT s.id as id, s.title as title, s.description as description, u.id as createdById, e.id as assignedTo, " +
            "s.createdDate as createdDate, s.updatedDate as updatedDate " +
            "FROM SuggestionEntity s LEFT JOIN s.createdBy u LEFT JOIN s.assignedTo e WHERE s.visible = true order by s.createdDate desc")
    Page<SuggestionMapper> findAllPageble(Pageable pageable);

}
