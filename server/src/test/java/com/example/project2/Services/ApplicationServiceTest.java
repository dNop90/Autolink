package com.example.project2.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Application;
import com.example.project2.Exceptions.AccountNotFoundException;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Repositories.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Unit test for "apply" method
     */
    @Test
    public void testApply_Success() {
        // Arrange
        Application application = new Application();
        when(applicationRepository.save(application)).thenReturn(application);

        // Act
        String result = applicationService.apply(application);

        // Assert
        assertEquals("Success", result);
        verify(applicationRepository, times(1)).save(application);
    }

    /**
     * Unit test for "getPendingApplications" method
     */
    @Test
    public void testGetPendingApplications_Success() {
        // Arrange
        List<Application> pendingApplications = Arrays.asList(new Application(), new Application());
        when(applicationRepository.getPendingApplication("Pending")).thenReturn(pendingApplications);

        // Act
        List<Application> result = applicationService.getPendingApplications();

        // Assert
        assertEquals(2, result.size());
        assertEquals(pendingApplications, result);
    }

    /**
     * Unit test for "getApplicationByAccountId" method
     */
    @Test
    public void testGetApplicationByAccountId_Success() {
        // Arrange
        Integer accountId = 1;
        List<Application> applications = Arrays.asList(new Application(), new Application());
        when(applicationRepository.getApplicationByAccountId(accountId)).thenReturn(applications);

        // Act
        List<Application> result = applicationService.geApplicationByAccountId(accountId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(applications, result);
    }

    /**
     * Unit test for "approve" method
     */
    @Test
    public void testApprove_ApprovedStatus_Success() throws AccountNotFoundException {
        // Arrange
        Long applicationId = 1L;
        String status = "Approved";
        Long applicantId = 2L;
        Account account = new Account();
        when(applicationRepository.findAccountId(applicationId)).thenReturn(applicantId);
        when(accountRepository.findById(applicantId)).thenReturn(Optional.of(account));

        // Act
        String result = applicationService.approve(applicationId, status);

        // Assert
        assertEquals("Approved", result);
        verify(applicationRepository, times(1)).updateStatus(applicationId, status);
        verify(accountRepository, times(1)).updateRole(applicantId, 2);
    }

    @Test
    public void testApprove_RejectedStatus_Success() throws AccountNotFoundException {
        // Arrange
        Long applicationId = 1L;
        String status = "Rejected";
        Long applicantId = 2L;
        when(applicationRepository.findAccountId(applicationId)).thenReturn(applicantId);

        // Act
        String result = applicationService.approve(applicationId, status);

        // Assert
        assertEquals("Rejected", result);
        verify(applicationRepository, times(1)).updateStatus(applicationId, status);
        verify(accountRepository, never()).updateRole(anyLong(), anyInt());
    }

    @Test
    public void testApprove_ApprovedStatus_AccountNotFound_ThrowsException() {
        // Arrange
        Long applicationId = 1L;
        String status = "Approved";
        Long applicantId = 2L;
        when(applicationRepository.findAccountId(applicationId)).thenReturn(applicantId);
        when(accountRepository.findById(applicantId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> applicationService.approve(applicationId, status));
        verify(applicationRepository, never()).updateStatus(applicationId, status);
        verify(accountRepository, never()).updateRole(anyLong(), anyInt());
    }

    /**
     * Code Review Feedback:
     *
     * 1. Null Safety: Consider using `Optional` for methods like `applicationRepository.findAccountId` to handle null values more gracefully.
     * 2. Exception Handling: Add custom error messages for exceptions like `AccountNotFoundException` to provide more context.
     * 3. Logging: Add logging statements for key actions like approving/rejecting applications for easier debugging.
     * 4. Transaction Management: Ensure database updates (e.g., `updateStatus` and `updateRole`) are atomic to avoid data inconsistencies.
     * 5. Validation: Add validation checks for `status` values (e.g., only "Approved" or "Rejected" are allowed).
     */
}
