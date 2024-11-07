package com.sharafutdinov.aircraft_maintenance.model;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerformedWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String workName;
    private String description;
    private LocalDateTime completionDate;
    @Enumerated(EnumType.STRING)
    private WorkResult result;

    @ManyToOne
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

    @ManyToOne
    @JoinColumn(name = "engineer_id")
    private Engineer engineer;
}
