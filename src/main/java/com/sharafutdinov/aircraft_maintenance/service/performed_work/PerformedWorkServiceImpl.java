package com.sharafutdinov.aircraft_maintenance.service.performed_work;

import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTO;
import com.sharafutdinov.aircraft_maintenance.dto.performed_work.PerformedWorkDTOMapper;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceNotFoundException;
import com.sharafutdinov.aircraft_maintenance.exceptions.ResourceAlreadyFoundException;
import com.sharafutdinov.aircraft_maintenance.model.Airplane;
import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.model.PerformedWork;
import com.sharafutdinov.aircraft_maintenance.repository.AirplaneRepository;
import com.sharafutdinov.aircraft_maintenance.repository.PerformedWorkRepository;
import com.sharafutdinov.aircraft_maintenance.request.PerformedWorkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformedWorkServiceImpl implements PerformedWorkService {
    private final PerformedWorkRepository performedWorkRepository;
    private final AirplaneRepository airplaneRepository;
    private final PerformedWorkDTOMapper performedWorkDTOMapper;
    @Override
    public PerformedWorkDTO addPerformedWork(PerformedWorkRequest request, Engineer engineer) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(request.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        return Optional.of(request)
                .filter(p -> !performedWorkRepository.existsByWorkName(request.getWorkName()))
                .map(req ->{
                    PerformedWork performedWork = new PerformedWork();
                    performedWork.setAirplane(airplane);
                    performedWork.setWorkName(request.getWorkName());
                    performedWork.setEngineer(engineer);
                    performedWork.setDescription(request.getDescription());
                    performedWork.setCompletionDate(request.getCompletionDate());
                    performedWork.setResult(request.getResult());
                    return performedWorkRepository.save(performedWork);
                })
                .map(performedWorkDTOMapper)
                .orElseThrow(() -> new ResourceAlreadyFoundException("уже есть данные об этой проверке: " +
                        request.getWorkName() ));
    }

    @Override
    public PerformedWorkDTO updatePerformedWork(PerformedWorkRequest request, Long performedWorkId) {
        Airplane airplane = Optional.ofNullable(airplaneRepository.findBySerialNumber(request.getAirplaneSerialNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Такого самолета нет в БД"));

        return performedWorkRepository.findById(performedWorkId)
                .map(updatedPerformedwork -> {
                    updatedPerformedwork.setAirplane(airplane);
                    updatedPerformedwork.setWorkName(request.getWorkName());
                    updatedPerformedwork.setDescription(request.getDescription());
                    updatedPerformedwork.setCompletionDate(request.getCompletionDate());
                    updatedPerformedwork.setResult(request.getResult());
                    return performedWorkRepository.save(updatedPerformedwork);
                })
                .map(performedWorkDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("такой записи нету в БД"));
    }

    @Override
    public List<PerformedWorkDTO> getAllPerformedWorksByEngineerId(Long engineerId) {
        List<PerformedWork> performedWorksList = Optional.ofNullable(performedWorkRepository.findByEngineerId(engineerId))
                .orElseThrow(() -> new ResourceNotFoundException("У этого инженера еще нет записей"));

        return performedWorksList
                .stream()
                .map(performedWorkDTOMapper)
                .toList();
    }
}
