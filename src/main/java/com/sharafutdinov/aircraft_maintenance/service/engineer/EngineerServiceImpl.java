package com.sharafutdinov.aircraft_maintenance.service.engineer;

import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerDTO;
import com.sharafutdinov.aircraft_maintenance.dto.engineer.EngineerDTOMapper;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.repository.EngineerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EngineerServiceImpl implements EngineerService{
    private final EngineerRepository engineerRepository;
    private final EngineerDTOMapper engineerDTOMapper;

    @Override
    public EngineerDTO getEngineerById(Long engineerId) {
        return engineerRepository.findById(engineerId)
                .map(engineerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Такого инженера нет в БД"));
    }

    @Override
    public Engineer getAuthenticatedEngineer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return engineerRepository.findByUsername(username);
    }
}
