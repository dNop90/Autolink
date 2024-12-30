package com.example.project2.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Application;
import com.example.project2.Repositories.ApplicationRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    /*
     * Dealer Apply service
     * Save applications to database
     * 
     * @param application, application with all fields
     * 
     * @return success message
     */
    public String apply(Application application) {
        applicationRepository.save(application);
        return "Success";
    }

    public List<Application> getDealerApplications() {
        return applicationRepository.findAll();
    }
}
