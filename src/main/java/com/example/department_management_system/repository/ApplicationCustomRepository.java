package com.example.department_management_system.repository;

import com.example.department_management_system.dto.ApplicationFilterDTO;
import com.example.department_management_system.entity.ApplicationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApplicationCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<ApplicationEntity> filter(ApplicationFilterDTO applicationFilterDTO, int page, int size) {
        StringBuilder query = new StringBuilder(" where a.visible = true ");
        Map<String, Object> params = new HashMap<>();
        if (applicationFilterDTO.getId() != null) {
            query.append(" and a.id = :id ");
            params.put("id", applicationFilterDTO.getId());
        }
        if (applicationFilterDTO.getTitle() != null) {
            query.append(" and lower(a.title) like :title ");
            params.put("title", "%" + applicationFilterDTO.getTitle() + "%");
        }
        if (applicationFilterDTO.getDescription() != null) {
            query.append(" and lower(a.description) like :description ");
            params.put("description", "%" + applicationFilterDTO.getDescription().toLowerCase() + "%");
        }
        if (applicationFilterDTO.getCreatedDateTo() != null && applicationFilterDTO.getCreatedDateFrom() != null) {
            query.append(" and a.createdDate between :createdDateFrom and :createdDateTo ");
            params.put("createdDateFrom", LocalDateTime.of(applicationFilterDTO.getCreatedDateFrom(), LocalTime.MIN));
            params.put("createdDateTo", LocalDateTime.of(applicationFilterDTO.getCreatedDateTo(), LocalTime.MAX));
        } else if (applicationFilterDTO.getCreatedDateFrom() != null) {
            query.append(" and a.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", LocalDateTime.of(applicationFilterDTO.getCreatedDateFrom(), LocalTime.MIN));
        } else if (applicationFilterDTO.getCreatedDateTo() != null) {
            query.append(" and a.createdDate <= :createdDateTo ");
            params.put("createdDateTo", LocalDateTime.of(applicationFilterDTO.getCreatedDateTo(), LocalTime.MAX));
        }
        if (applicationFilterDTO.getUpdatedDateTo() != null && applicationFilterDTO.getUpdatedDateFrom() != null) {
            query.append(" and a.updatedDate between :updatedDateFrom and :updatedDateTo ");
            params.put("updatedDateFrom", LocalDateTime.of(applicationFilterDTO.getUpdatedDateFrom(), LocalTime.MIN));
            params.put("updatedDateTo", LocalDateTime.of(applicationFilterDTO.getUpdatedDateTo(), LocalTime.MAX));
        } else if (applicationFilterDTO.getUpdatedDateFrom() != null) {
            query.append(" and a.updatedDate >= :updatedDateFrom ");
            params.put("updatedDateFrom", LocalDateTime.of(applicationFilterDTO.getUpdatedDateFrom(), LocalTime.MIN));
        } else if (applicationFilterDTO.getUpdatedDateTo() != null) {
            query.append(" and a.updatedDate <= :updatedDateTo ");
            params.put("updatedDateTo", LocalDateTime.of(applicationFilterDTO.getUpdatedDateTo(), LocalTime.MAX));
        }
        if (applicationFilterDTO.getOfferingId() != null) {
            query.append(" and a.offering.id = :offeringId ");
            params.put("offeringId", applicationFilterDTO.getOfferingId());
        }
        if (applicationFilterDTO.getCreatedById() != null) {
            query.append(" and a.createdBy.id = :createdById ");
            params.put("createdById", applicationFilterDTO.getCreatedById());
        }
        if (applicationFilterDTO.getAssignedToId() != null) {
            query.append(" and a.assignedTo.id = :assignedToId ");
            params.put("assignedToId", applicationFilterDTO.getAssignedToId());
        }
        if (applicationFilterDTO.getDepartmentId() != null) {
            query.append(" and a.department.id = :departmentId ");
            params.put("departmentId", applicationFilterDTO.getDepartmentId());
        }
        if (applicationFilterDTO.getStatus() != null) {
            query.append(" and a.status = :status ");
            params.put("status", applicationFilterDTO.getStatus());
        }
        if (applicationFilterDTO.getCompletedWorkId() != null) {
            query.append(" and a.completedWork.id = :completedWorkId ");
            params.put("completedWorkId", applicationFilterDTO.getCompletedWorkId());
        }

        StringBuilder selectBuilder = new StringBuilder("select a from ApplicationEntity a ");
        selectBuilder.append(query);
        selectBuilder.append(" order by a.createdDate desc");

        StringBuilder countBuilder = new StringBuilder("select count(a) from ApplicationEntity a ");
        countBuilder.append(query);

        ///  Content
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), ApplicationEntity.class);
        params.forEach(selectQuery::setParameter);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);  // limit size
        List<ApplicationEntity> content = selectQuery.getResultList();

        ///  Tootal Count
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        params.forEach(countQuery::setParameter);
        Long totalCount = (Long) countQuery.getSingleResult();

        return new PageImpl<>(content, PageRequest.of(page, size), totalCount);
    }

}
