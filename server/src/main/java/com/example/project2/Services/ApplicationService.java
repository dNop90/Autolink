package com.example.project2.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Application;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Repositories.ApplicationRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final AccountRepository accountRepository;

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

    public List<Application> getPendingApplications() {
        return applicationRepository.getPendingApplication("Pending");
    }

    public List<Application> geApplicationByAccountId(Integer accountId) {
        return applicationRepository.getApplicationByAccountId(accountId);
    }

    public String approve(Long applicationId, String status) throws AccountNotFoundException {
        Long applicantId = applicationRepository.findAccountId(applicationId);
        Optional<Account> check = accountRepository.findById((long) applicantId);
        if (status.equals("Approved")) {
            if (check.isPresent()) {
                applicationRepository.updateStatus(applicationId, status);
                accountRepository.updateRole(applicantId, 2);
                return status;
            } else
                throw new AccountNotFoundException();
        }
        applicationRepository.updateStatus(applicationId, status);
        return status;
    }
}
