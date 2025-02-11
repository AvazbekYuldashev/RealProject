package com.example.department_management_system.repository;

import com.example.department_management_system.dto.EmployeeFilterDTO;
import com.example.department_management_system.entity.EmployeeEntity;
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
public class EmployeeCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public PageImpl<EmployeeEntity> filter(EmployeeFilterDTO employeeDTO, int page, int size) {
        StringBuilder query = new StringBuilder(" where e.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (employeeDTO.getId() != null) {
            query.append(" and e.id = :id ");
            params.put("id", employeeDTO.getId());
        }
        if (employeeDTO.getName() != null) {
            query.append(" and e.name like :name ");
            params.put("name", "%" + employeeDTO.getName() + "%");
        }
        if (employeeDTO.getSurname() != null) {
            query.append(" and e.surname like :surname ");
            params.put("surname", "%" + employeeDTO.getSurname() + "%");
        }
        if (employeeDTO.getRole() != null) {
            query.append(" and e.role = :role ");
            params.put("role", employeeDTO.getRole());
        }
        if (employeeDTO.getPosition() != null) {
            query.append(" and lower(e.position) like :position ");
            params.put("position", "%" + employeeDTO.getPosition().toLowerCase() + "%");
        }
        if (employeeDTO.getStatus() != null) {
            query.append(" and e.status = :status ");
            params.put("status", employeeDTO.getStatus());
        }
        if (employeeDTO.getCreatedDateTo() != null && employeeDTO.getCreatedDateFrom() != null) {
            query.append(" and e.createdDate between :createdDateFrom and :createdDateTo ");
            params.put("createdDateFrom", LocalDateTime.of(employeeDTO.getCreatedDateFrom(), LocalTime.MIN) );
            params.put("createdDateTo", LocalDateTime.of(employeeDTO.getCreatedDateTo(), LocalTime.MAX) );
        } else if (employeeDTO.getCreatedDateFrom() != null) {
            query.append(" and e.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", LocalDateTime.of(employeeDTO.getCreatedDateFrom(), LocalTime.MIN) );
        } else if (employeeDTO.getCreatedDateTo() != null) {
            query.append(" and e.createdDate <= :createdDateTo ");
            params.put("createdDateTo", LocalDateTime.of(employeeDTO.getCreatedDateTo(), LocalTime.MAX) );
        }
        if (employeeDTO.getUpdatedDateTo() != null && employeeDTO.getUpdatedDateFrom() != null) {
            query.append(" and e.updatedDate between :updatedDateFrom and :updatedDateTo ");
            params.put("updatedDateFrom", LocalDateTime.of(employeeDTO.getUpdatedDateFrom(), LocalTime.MIN));
            params.put("updatedDateTo", LocalDateTime.of(employeeDTO.getUpdatedDateTo(), LocalTime.MAX) );
        } else if (employeeDTO.getUpdatedDateFrom() != null) {
            query.append(" and e.updatedDate >= :updatedDateFrom ");
            params.put("updatedDateFrom", LocalDateTime.of(employeeDTO.getUpdatedDateFrom(), LocalTime.MIN));
        } else if (employeeDTO.getUpdatedDateTo() != null) {
            query.append(" and e.updatedDate <= :updatedDateTo ");
            params.put("updatedDateTo", LocalDateTime.of(employeeDTO.getUpdatedDateTo(), LocalTime.MAX) );
        }
        if (employeeDTO.getDepartmentId() != null) {
            query.append(" and e.department.id = :departmentId ");
            params.put("departmentId", employeeDTO.getDepartmentId());
        }
        if (employeeDTO.getEmail() != null) {
            query.append(" and e.login = :login ");
            params.put("login", employeeDTO.getEmail());
        }
        if (employeeDTO.getPassword() != null) {
            query.append(" and e.password = :password ");
            params.put("password", employeeDTO.getPassword());
        }

        StringBuilder selectBuilder = new StringBuilder("select e from EmployeeEntity e ");
        selectBuilder.append(query);
        selectBuilder.append(" order by e.createdDate desc");

        StringBuilder countBuilder = new StringBuilder("select count(e) from EmployeeEntity e ");
        countBuilder.append(query);

        ///  Content
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), EmployeeEntity.class);
        params.forEach(selectQuery::setParameter);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);  // limit size
        List<EmployeeEntity> content = selectQuery.getResultList();

        ///  Tootal Count
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        params.forEach(countQuery::setParameter);
        Long totalCount = (Long) countQuery.getSingleResult();

        return new PageImpl<>(content, PageRequest.of(page, size), totalCount);

    }

}
