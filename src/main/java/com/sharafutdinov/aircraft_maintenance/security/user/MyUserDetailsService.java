package com.sharafutdinov.aircraft_maintenance.security.user;

import com.sharafutdinov.aircraft_maintenance.model.Engineer;
import com.sharafutdinov.aircraft_maintenance.repository.EngineerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final EngineerRepository engineerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Engineer engineer = Optional.ofNullable(engineerRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Такого логина нет в БД"));
        return UserPrincipal.buildUserDetails(engineer);
    }
}
