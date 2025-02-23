package com.example.department_management_system.repository;


import com.example.department_management_system.dto.DepartmentFilterDTO;
import com.example.department_management_system.entity.DepartmentEntity;
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
public class DepartmentCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<DepartmentEntity> filter(DepartmentFilterDTO departmentDTO, int page, int size) {
        StringBuilder query = new StringBuilder(" where d.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (departmentDTO.getId() != null) {
            query.append(" and d.id = :id ");
            params.put("id", departmentDTO.getId());
        }
        if (departmentDTO.getStatus() != null) {
            query.append(" and d.status = :status ");
            params.put("status", departmentDTO.getStatus());
        }
        if (departmentDTO.getTitle() != null) {
            query.append(" and lower(d.title) like :title ");
            params.put("title", "%" + departmentDTO.getTitle().toLowerCase() + "%");
        }
        if (departmentDTO.getDescription() != null) {
            query.append(" and lower(d.description) like :description ");
            params.put("description", "%" + departmentDTO.getDescription().toLowerCase() + "%");
        }
        if (departmentDTO.getAddress() != null) {
            query.append(" and lower(d.address) like :address ");
            params.put("address", "%" + departmentDTO.getAddress().toLowerCase() + "%");
        }
        if (departmentDTO.getPhoneNumber() != null) {
            query.append(" and lower(d.phoneNumber) = :phoneNumber ");
            params.put("phoneNumber", departmentDTO.getPhoneNumber());
        }
        if (departmentDTO.getHeadOfDepartment() != null) {
            query.append(" and lower(d.headOfDepartment) like :headOfDepartment ");
            params.put("headOfDepartment", "%" + departmentDTO.getHeadOfDepartment().toLowerCase() + "%");
        }
        if (departmentDTO.getType() != null) {
            query.append(" and d.type = :type ");
            params.put("type", departmentDTO.getType());
        }

        if (departmentDTO.getCreatedDateTo() != null && departmentDTO.getCreatedDateFrom() != null) {
            query.append(" and d.createdDate between :createdDateFrom and :createdDateTo ");
            params.put("createdDateFrom", LocalDateTime.of(departmentDTO.getCreatedDateFrom(), LocalTime.MIN) );
            params.put("createdDateTo", LocalDateTime.of(departmentDTO.getCreatedDateTo(), LocalTime.MAX) );
        } else if (departmentDTO.getCreatedDateFrom() != null) {
            query.append(" and d.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", LocalDateTime.of(departmentDTO.getCreatedDateFrom(), LocalTime.MIN) );
        } else if (departmentDTO.getCreatedDateTo() != null) {
            query.append(" and d.createdDate <= :createdDateTo ");
            params.put("createdDateTo", LocalDateTime.of(departmentDTO.getCreatedDateTo(), LocalTime.MAX) );
        }

        if (departmentDTO.getUpdatedDateTo() != null && departmentDTO.getUpdatedDateFrom() != null) {
            query.append(" and d.updatedDate between :updatedDateFrom and :updatedDateTo ");
            params.put("updatedDateFrom", LocalDateTime.of(departmentDTO.getUpdatedDateFrom(), LocalTime.MIN));
            params.put("updatedDateTo", LocalDateTime.of(departmentDTO.getUpdatedDateTo(), LocalTime.MAX) );
        } else if (departmentDTO.getUpdatedDateFrom() != null) {
            query.append(" and d.updatedDate >= :updatedDateFrom ");
            params.put("updatedDateFrom", LocalDateTime.of(departmentDTO.getUpdatedDateFrom(), LocalTime.MIN));
        } else if (departmentDTO.getUpdatedDateTo() != null) {
            query.append(" and d.updatedDate <= :updatedDateTo ");
            params.put("updatedDateTo", LocalDateTime.of(departmentDTO.getUpdatedDateTo(), LocalTime.MAX) );
        }

        StringBuilder selectBuilder = new StringBuilder("select d from DepartmentEntity d ");
        selectBuilder.append(query);
        selectBuilder.append(" order by d.createdDate desc");

        StringBuilder countBuilder = new StringBuilder("select count(d) from DepartmentEntity d ");
        countBuilder.append(query);

        ///  Content
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), DepartmentEntity.class);
        params.forEach(selectQuery::setParameter);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);  // limit size
        List<DepartmentEntity> content = selectQuery.getResultList();

        ///  Tootal Count
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        params.forEach(countQuery::setParameter);
        Long totalCount = (Long) countQuery.getSingleResult();

        return new PageImpl<>(content, PageRequest.of(page, size), totalCount);
    }

}
