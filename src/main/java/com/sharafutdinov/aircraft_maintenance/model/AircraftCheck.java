package com.sharafutdinov.aircraft_maintenance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AircraftCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String checkName;

    @OneToMany(mappedBy = "aircraftCheck",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformedWork> performedWork;
}
