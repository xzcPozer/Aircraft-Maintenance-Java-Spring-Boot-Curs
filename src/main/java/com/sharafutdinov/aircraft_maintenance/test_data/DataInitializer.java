package com.sharafutdinov.aircraft_maintenance.test_data;

import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.repository.EngineerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final EngineerRepository engineerRepository;
    private final AirplaneRepository airplaneRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
        createDefaultAirplaneIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        for (int i = 1; i <= 5; i++) {
            String username = "User" + i;
            if (engineerRepository.existsByUsername(username))
                continue;
            Engineer user = new Engineer();
            user.setFirstName("User first name" + i);
            user.setLastName("User last name" + i);
            user.setPosition("Инженер");
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("user" + i));
            engineerRepository.save(user);
        }
    }

    private void createDefaultAirplaneIfNotExists() {
        for (int i = 1; i <= 10; i++) {
            String serialNumber = "FJHWEKRY" + i;
            if(airplaneRepository.existsBySerialNumber(serialNumber))
                continue;
            Airplane airplane = new Airplane();
            airplane.setModel("Airplane model" + i);
            airplane.setSerialNumber(serialNumber);
            airplane.setYearOfRelease(LocalDate.of(2022, 3, 23));
            airplaneRepository.save(airplane);
        }
    }
}
