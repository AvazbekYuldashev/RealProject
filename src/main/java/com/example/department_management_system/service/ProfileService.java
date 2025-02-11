package com.example.department_management_system.service;

import com.example.department_management_system.config.CustomUserDetails;
import com.example.department_management_system.dto.EmployeeDTO;
import com.example.department_management_system.dto.auth.AuthDTO;
import com.example.department_management_system.dto.auth.ResponseDTO;
import com.example.department_management_system.entity.EmployeeEntity;
import com.example.department_management_system.enums.EmployeeStatus;
import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.repository.EmployeeRepository;

import com.example.department_management_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private EmployeeRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bc;
    @Autowired
    private AuthenticationManager authenticationManager;

    public EmployeeDTO registration(EmployeeDTO dto){
        Optional<EmployeeEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent()){
            throw new AppBadRequestExeption("Email already exists");
        }
        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setRole(dto.getRole());
        entity.setPosition(dto.getPosition());
        entity.setStatus(EmployeeStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setEmail(dto.getEmail());
        entity.setPassword(bc.encode(dto.getPassword()));
        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setStatus(entity.getStatus());
        dto.setVisible(entity.getVisible());
        return dto;
    }


    public ResponseDTO authorization(AuthDTO authDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();

                ResponseDTO response = new ResponseDTO();
                response.setToken(JwtUtil.encode(profile.getId(), profile.getName(), profile.getSurname(), profile.getRole(), profile.getPosition(), profile.getStatus(), profile.getDepartmentId(), profile.getVisible(), profile.getEmail()));
                return response;
            }
            throw new AppBadRequestExeption("Phone or password wrong");
        } catch (BadCredentialsException e) {
            throw new AppBadRequestExeption("Phone or password wrong");
        }
    }



}
