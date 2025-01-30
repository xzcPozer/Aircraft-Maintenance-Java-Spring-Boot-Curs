package com.sharafutdinov.aircraft_maintenance.model;

import com.sharafutdinov.aircraft_maintenance.enums.CheckStatus;
import com.sharafutdinov.aircraft_maintenance.enums.CheckType;
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
public class ScheduledCheck extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckType type;
    @Column(nullable = false)
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckStatus status;
    @Column(nullable = false)
    private boolean notificationSent;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @JoinColumn(name = "engineer_id", nullable = false)
    private String engineerId;
}
