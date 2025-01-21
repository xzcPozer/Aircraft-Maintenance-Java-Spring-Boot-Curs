package com.sharafutdinov.aircraft_maintenance.test_data;

import com.sharafutdinov.aircraft_maintenance.model.AircraftCheck;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.repository.AircraftCheckRepository;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final AirplaneRepository airplaneRepository;
    private final AircraftCheckRepository aircraftCheckRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultAirplaneIfNotExists();
        createDefaultAircraftChecksIfNotExists();
    }

    private void createDefaultAircraftChecksIfNotExists() {
        for (int i = 1; i <= 10; i++) {
            String check = "Check " + i;
            if (aircraftCheckRepository.existsByCheckName(check))
                continue;
            AircraftCheck aircraftCheck = new AircraftCheck();
            aircraftCheck.setCheckName(check);
            aircraftCheckRepository.save(aircraftCheck);
        }
    }

    private void createDefaultAirplaneIfNotExists() {
        for (int i = 1; i <= 10; i++) {
            String serialNumber = "FJHWEKRY" + i;
            if (airplaneRepository.existsBySerialNumber(serialNumber))
                continue;
            Airplane airplane = new Airplane();
            airplane.setModel("Airplane model" + i);
            airplane.setSerialNumber(serialNumber);
            airplane.setYearOfRelease(LocalDate.of(2022, 3, 23));
            airplaneRepository.save(airplane);
        }
    }
}
