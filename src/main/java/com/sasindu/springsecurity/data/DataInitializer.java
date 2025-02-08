package com.sasindu.springsecurity.data;

import com.sasindu.springsecurity.abstractions.enums.UserRole;
import com.sasindu.springsecurity.entities.Role;
import com.sasindu.springsecurity.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final IRoleRepository IRoleRepository;

    /**
     * Initialize some seed data on application startup
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeRoles();
    }


    /**
     * Initialize roles in application startup
     */
    private void initializeRoles() {
        List<UserRole> roles = Arrays.asList(UserRole.ADMIN, UserRole.USER);
        for (UserRole role : roles) {
            if (!IRoleRepository.existsByName(role)) {
                IRoleRepository.save(new Role(null, role));
            }
        }
    }
}