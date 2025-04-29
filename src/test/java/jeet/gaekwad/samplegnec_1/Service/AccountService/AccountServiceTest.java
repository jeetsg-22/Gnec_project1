package jeet.gaekwad.samplegnec_1.Service.AccountService;

import jeet.gaekwad.samplegnec_1.Exception.InvalidAccountException;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.reactivestreams.Publisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateAccount_validAccount_shouldSave() {
        Accounts account = new Accounts();
        account.setUsername("jeet");
        account.setPassword("password");

        when(accountRepository.findByUsername("jeet")).thenReturn(null); // No existing user
        when(accountRepository.save(account)).thenReturn(account); // Simulate save

        Accounts savedAccount = accountService.generateAccount(account);

        assertNotNull(savedAccount);
        assertEquals("jeet", savedAccount.getUsername());
        verify(accountRepository).save(account); // Optional: verifies save() was called
    }
    @Test
    void getAccounts_shouldReturnListOfAccounts() {
        List<Accounts> accountsList = new ArrayList<>();
        accountsList.add(new Accounts());
        accountsList.add(new Accounts());

        when(accountRepository.findAll()).thenReturn(accountsList);

        List<Accounts> result = accountService.getAccounts();

        assertEquals(2, result.size());
    }

    @Test
    void deleteAccounts_validId_shouldDeleteAccount() {
        Accounts account = new Accounts();
        account.setAccountId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Accounts result = accountService.deleteAccounts(1L);

        verify(accountRepository).delete(account);
        assertEquals(1L, result.getAccountId());
    }

    @Test
    void deleteAccounts_invalidId_shouldThrowException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.deleteAccounts(1L));

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void updateAccounts_nonExistingAccount_shouldThrowException() {
        when(accountRepository.findByAccountIdAndUsername(1L, "jeet")).thenReturn(null);

        Accounts account = new Accounts();
        account.setUsername("jeet");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.updateAccounts(account, "jeet", 1L));

        assertEquals("Account not found to be updated", exception.getMessage());
    }


    @Test
    void loadUserByUsername_validUser_shouldReturnUserDetails() {
        Accounts account = new Accounts();
        account.setUsername("jeet");
        account.setPassword("password");
        account.setRole("USER");

        when(accountRepository.findByUsername("jeet")).thenReturn(account); // ✅ Return Accounts object directly

        UserDetails userDetails = accountService.loadUserByUsername("jeet");

        assertEquals("jeet", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_invalidUser_shouldThrowException() {
        when(accountRepository.findByUsername("invalid")).thenReturn(null);

        assertThrows(InvalidAccountException.class, () ->
                accountService.loadUserByUsername("invalid"));
    }
}
