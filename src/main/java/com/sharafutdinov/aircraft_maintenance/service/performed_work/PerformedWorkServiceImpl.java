package com.sharafutdinov.aircraft_maintenance.service.performed_work;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.AuthPerformedWorkDTOMapper;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTOMapper;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.model.AircraftCheck;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import com.sharafutdinov.aircraft_maintenance.repository.AircraftCheckRepository;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.repository.PerformedWorkRepository;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;
import com.sharafutdinov.aircraft_maintenance.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerformedWorkServiceImpl implements PerformedWorkService {
    private final PerformedWorkRepository performedWorkRepository;
    private final AirplaneRepository airplaneRepository;
    private final AuthPerformedWorkDTOMapper authPerformedWorkDTOMapper;
    private final PerformedWorkDTOMapper performedWorkDTOMapper;
    private final AircraftCheckRepository aircraftCheckRepository;

    @Override
    public AuthPerformedWorkDTO addPerformedWork(PerformedWorkRequest request, String engineerId) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(request.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        AircraftCheck aircraftCheck = Optional.ofNullable(aircraftCheckRepository.findByCheckName(request.getWorkName()))
                .orElseThrow(() -> new ResourceNotFoundException("Такой проверки нет в БД"));

        return Optional.of(request)
                .map(req -> {
                    PerformedWork performedWork = new PerformedWork();
                    performedWork.setAirplane(airplane);
                    performedWork.setAircraftCheck(aircraftCheck);
                    performedWork.setEngineerId(engineerId);
                    performedWork.setDescription(request.getDescription());
                    performedWork.setCompletionDate(request.getCompletionDate());
                    performedWork.setResult(request.getResult());
                    return performedWorkRepository.save(performedWork);
                })
                .map(authPerformedWorkDTOMapper)
                .orElseGet(null);
    }

    @Override
    public AuthPerformedWorkDTO updatePerformedWork(PerformedWorkRequest request, Long performedWorkId, String userId) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(request.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        AircraftCheck aircraftCheck = Optional.ofNullable(aircraftCheckRepository.findByCheckName(request.getWorkName()))
                .orElseThrow(() -> new ResourceNotFoundException("Такой проверки нет в БД"));

        return Optional.ofNullable(performedWorkRepository.findByIdAndEngineerId(performedWorkId, userId))
                .map(updatedPerformedwork -> {
                    updatedPerformedwork.setAirplane(airplane);
                    updatedPerformedwork.setAircraftCheck(aircraftCheck);
                    updatedPerformedwork.setDescription(request.getDescription());
                    updatedPerformedwork.setCompletionDate(request.getCompletionDate());
                    updatedPerformedwork.setResult(request.getResult());
                    return performedWorkRepository.save(updatedPerformedwork);
                })
                .map(authPerformedWorkDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("У вас нет такой записи в БД"));
    }

    @Override
    public PageResponse<PerformedWorkDTO> getAllPerformedWorks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PerformedWork> performedWorks = performedWorkRepository.findAll(pageable);
        List<PerformedWorkDTO> performedWorkResponse = performedWorks.stream()
                .map(performedWorkDTOMapper)
                .toList();

        return new PageResponse<>(
                performedWorkResponse,
                performedWorks.getNumber(),
                performedWorks.getSize(),
                performedWorks.getTotalElements(),
                performedWorks.getTotalPages(),
                performedWorks.isFirst(),
                performedWorks.isLast()
        );
    }

    @Override
    public AuthPerformedWorkDTO getWorkByIdAndEngineer(Long performedWorkId, String engineerId) {
        PerformedWork pw = Optional.ofNullable(performedWorkRepository.findByIdAndEngineerId(performedWorkId, engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У вас нет такой работы в БД"));

        return authPerformedWorkDTOMapper.apply(pw);
    }

    @Override
    public PageResponse<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndPeriod(int page, int size, String engineerId, Date date) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("completionDate").descending());
        Page<PerformedWork> performedWorks = Optional.ofNullable(performedWorkRepository.findByEngineerIdAndCompletionDate(pageable, engineerId, date))
                .orElseThrow(() -> new ResourceNotFoundException("Нет записей на эту дату"));
        List<AuthPerformedWorkDTO> performedWorkResponse = performedWorks.stream()
                .map(authPerformedWorkDTOMapper)
                .toList();

        return new PageResponse<>(
                performedWorkResponse,
                performedWorks.getNumber(),
                performedWorks.getSize(),
                performedWorks.getTotalElements(),
                performedWorks.getTotalPages(),
                performedWorks.isFirst(),
                performedWorks.isLast()
        );
    }

    @Override
    public PageResponse<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerId(int page, int size, String engineerId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PerformedWork> performedWorks = Optional.ofNullable(performedWorkRepository.findByEngineerId(pageable, engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));
        List<AuthPerformedWorkDTO> performedWorkResponse = performedWorks.stream()
                .map(authPerformedWorkDTOMapper)
                .toList();

        return new PageResponse<>(
                performedWorkResponse,
                performedWorks.getNumber(),
                performedWorks.getSize(),
                performedWorks.getTotalElements(),
                performedWorks.getTotalPages(),
                performedWorks.isFirst(),
                performedWorks.isLast()
        );
    }

    @Override
    public List<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndTime(String engineerId, LocalDate date) {
        List<PerformedWork> performedWorksList = Optional.ofNullable(performedWorkRepository.findByEngineerId(engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));

        return performedWorksList
                .stream()
                .filter(r -> r.getCompletionDate().toLocalDate().isEqual(date))
                .map(authPerformedWorkDTOMapper)
                .toList();
    }

    @Override
    public List<AuthPerformedWorkDTO> getAllPerformedWorksByEngineerIdAndTime(String engineerId, LocalDate date1, LocalDate date2) {
        List<PerformedWork> performedWorksList = Optional.ofNullable(performedWorkRepository.findByEngineerId(engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));

        return performedWorksList
                .stream()
                .filter(r -> r.getCompletionDate().toLocalDate().isEqual(date2) || r.getCompletionDate().toLocalDate().isEqual(date1)
                        || (r.getCompletionDate().toLocalDate().isBefore(date2) && r.getCompletionDate().toLocalDate().isAfter(date1)))
                .map(authPerformedWorkDTOMapper)
                .toList();
    }

    @Override
    public List<AuthPerformedWorkDTO> getAllPerformedWorksBySerialNumberAndTime(String serialNumber, LocalDate date) {
        List<PerformedWork> performedWorksList = Optional.ofNullable(performedWorkRepository.findByAirplane_SerialNumber(serialNumber))
                .orElseThrow(() -> new ResourceNotFoundException("У этого самолета нет ни одной проверки"));

        return performedWorksList
                .stream()
                .filter(r -> r.getCompletionDate().toLocalDate().isEqual(date))
                .map(authPerformedWorkDTOMapper)
                .toList();
    }

    @Override
    public List<AuthPerformedWorkDTO> getAllPerformedWorksBySerialNumberAndTime(String serialNumber, LocalDate date1, LocalDate date2) {
        List<PerformedWork> performedWorksList = Optional.ofNullable(performedWorkRepository.findByAirplane_SerialNumber(serialNumber))
                .orElseThrow(() -> new ResourceNotFoundException("У этого самолета нет ни одной проверки"));

        return performedWorksList
                .stream()
                .filter(r -> r.getCompletionDate().toLocalDate().isEqual(date2) || r.getCompletionDate().toLocalDate().isEqual(date1)
                        || (r.getCompletionDate().toLocalDate().isBefore(date2) && r.getCompletionDate().toLocalDate().isAfter(date1)))
                .map(authPerformedWorkDTOMapper)
                .toList();
    }
}
