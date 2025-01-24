package com.sharafutdinov.aircraft_maintenance.service.airplane;

import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTO;
import com.sharafutdinov.aircraft_maintenance.dto.airplane.AirplaneDTOMapper;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirplaneDTOMapper airplaneDTOMapper;

    @Override
    public AirplaneDTO getAirplaneById(Long id) {
        return airplaneRepository.findById(id)
                .map(airplaneDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Самолет не был найден"));
    }

    @Override
    public AirplaneDTO getAirplaneBySerialNumber(String serialNumber) {
        return Optional.ofNullable(airplaneRepository.findBySerialNumber(serialNumber))
                .map(airplaneDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Самолет не был найден"));
    }

    @Override
    public PageResponse<AirplaneDTO> findAllAirplanes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Airplane> airplanes = airplaneRepository.findAll(pageable);
        List<AirplaneDTO> airplanesDto = airplanes.stream()
                .map(airplaneDTOMapper)
                .toList();

        return new PageResponse<>(
                airplanesDto,
                airplanes.getNumber(),
                airplanes.getSize(),
                airplanes.getTotalElements(),
                airplanes.getTotalPages(),
                airplanes.isFirst(),
                airplanes.isLast()
        );
    }

    @Override
    public List<String> findAllSerialNumbers() {
        List<Airplane> airplane = airplaneRepository.findAll();

        if (!airplane.isEmpty()) {
            return airplane.stream()
                    .map(Airplane::getSerialNumber)
                    .collect(Collectors.toList());
        }
        throw new ResourceNotFoundException("нет ни одного самолета в бд");
    }
}
