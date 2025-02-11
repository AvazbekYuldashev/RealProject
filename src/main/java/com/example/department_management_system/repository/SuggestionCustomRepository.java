package com.example.department_management_system.repository;

import com.example.department_management_system.dto.SuggestionFilterDTO;
import com.example.department_management_system.entity.SuggestionEntity;
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
public class SuggestionCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<SuggestionEntity> filter(SuggestionFilterDTO suggestionDTO, int page, int size) {
        StringBuilder query = new StringBuilder(" where s.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (suggestionDTO.getId() != null) {
            query.append(" and s.id = :id ");
            params.put("id", suggestionDTO.getId());
        }
        if (suggestionDTO.getTitle() != null) {
            query.append(" and lower(s.title) like :title ");
            params.put("title", "%" + suggestionDTO.getTitle().toLowerCase() + "%");
        }
        if (suggestionDTO.getDescription() != null) {
            query.append(" and lower(s.description) like :description ");
            params.put("description", "%" + suggestionDTO.getDescription().toLowerCase() + "%");
        }
        if (suggestionDTO.getCreatedDateTo() != null && suggestionDTO.getCreatedDateFrom() != null) {
            query.append(" and s.createdDate between :createdDateFrom and :createdDateTo ");
            params.put("createdDateFrom", LocalDateTime.of(suggestionDTO.getCreatedDateFrom(), LocalTime.MIN) );
            params.put("createdDateTo", LocalDateTime.of(suggestionDTO.getCreatedDateTo(), LocalTime.MAX) );
        } else if (suggestionDTO.getCreatedDateFrom() != null) {
            query.append(" and s.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", LocalDateTime.of(suggestionDTO.getCreatedDateFrom(), LocalTime.MIN) );
        } else if (suggestionDTO.getCreatedDateTo() != null) {
            query.append(" and s.createdDate <= :createdDateTo ");
            params.put("createdDateTo", LocalDateTime.of(suggestionDTO.getCreatedDateTo(), LocalTime.MAX) );
        }
        if (suggestionDTO.getUpdatedDateTo() != null && suggestionDTO.getUpdatedDateFrom() != null) {
            query.append(" and s.updatedDate between :updatedDateFrom and :updatedDateTo ");
            params.put("updatedDateFrom", LocalDateTime.of(suggestionDTO.getUpdatedDateFrom(), LocalTime.MIN));
            params.put("updatedDateTo", LocalDateTime.of(suggestionDTO.getUpdatedDateTo(), LocalTime.MAX) );
        } else if (suggestionDTO.getUpdatedDateFrom() != null) {
            query.append(" and s.updatedDate >= :updatedDateFrom ");
            params.put("updatedDateFrom", LocalDateTime.of(suggestionDTO.getUpdatedDateFrom(), LocalTime.MIN));
        } else if (suggestionDTO.getUpdatedDateTo() != null) {
            query.append(" and s.updatedDate <= :updatedDateTo ");
            params.put("updatedDateTo", LocalDateTime.of(suggestionDTO.getUpdatedDateTo(), LocalTime.MAX) );
        }

        if (suggestionDTO.getDepartmentId() != null) {
            query.append(" and s.department.id = :departmentId ");
            params.put("departmentId", suggestionDTO.getDepartmentId());
        }

        StringBuilder selectBuilder = new StringBuilder("select s from SuggestionEntity s ");
        selectBuilder.append(query);
        selectBuilder.append(" order by s.createdDate desc");

        StringBuilder countBuilder = new StringBuilder("select count(s) from SuggestionEntity s ");
        countBuilder.append(query);

        ///  Content
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), SuggestionEntity.class);
        params.forEach(selectQuery::setParameter);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);  // limit size
        List<SuggestionEntity> content = selectQuery.getResultList();

        ///  Tootal Count
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        params.forEach(countQuery::setParameter);
        Long totalCount = (Long) countQuery.getSingleResult();

        return new PageImpl<>(content, PageRequest.of(page, size), totalCount);

    }
}
