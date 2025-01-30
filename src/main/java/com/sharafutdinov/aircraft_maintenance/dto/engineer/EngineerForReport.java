package com.sharafutdinov.aircraft_maintenance.dto.engineer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Data
@AllArgsConstructor
public class EngineerForReport{
    private String lastname;
    private String name;
    private String position;
    private String period;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public EngineerForReport(JwtAuthenticationToken connectedEngineer, LocalDate date1) {
        this.lastname = connectedEngineer.getToken().getClaimAsString("family_name");
        this.name = connectedEngineer.getToken().getClaimAsString("given_name");
        this.position = getRole(connectedEngineer);
        if(this.position != null)
            this.position = this.position.substring(5);
        this.period = date1.format(formatter);
    }

    public EngineerForReport(JwtAuthenticationToken connectedEngineer, LocalDate date1, LocalDate date2) {
        this.lastname = connectedEngineer.getToken().getClaimAsString("family_name");
        this.name = connectedEngineer.getToken().getClaimAsString("given_name");
        this.position = getRole(connectedEngineer);
        if(this.position != null)
            this.position = this.position.substring(5);
        this.period = date1.format(formatter) +" - "+date2.format(formatter);
    }

    private String getRole(JwtAuthenticationToken jwt){
        Collection<? extends GrantedAuthority> authorities = jwt.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.equals("ROLE_ENGINEER") || a.equals("ROLE_SENIOR_ENGINEER"))
                .findFirst()
                .orElse(null);
    }

}
