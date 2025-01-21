package com.sharafutdinov.aircraft_maintenance.model;

import com.sharafutdinov.aircraft_maintenance.enums.WorkResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerformedWork extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    @Column(nullable = false)
    private LocalDateTime completionDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkResult result;

    @ManyToOne
    @JoinColumn(name = "aircraft_check_id", nullable = false)
    private AircraftCheck aircraftCheck;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

//  @ManyToOne
    @JoinColumn(name = "engineer_id", nullable = false)
    private String engineerId;
}
