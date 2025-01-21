package com.sharafutdinov.aircraft_maintenance.service.aircraft_check;

import com.sharafutdinov.aircraft_maintenance.model.AircraftCheck;
import com.sharafutdinov.aircraft_maintenance.repository.AircraftCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftCheckRepository repository;

    @Override
    public List<AircraftCheck> findAllAircraftCheck() {
        return repository.findAll();
    }
}
