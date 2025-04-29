package jeet.gaekwad.samplegnec_1.Service.AccountService;

import jeet.gaekwad.samplegnec_1.Exception.InvalidAccountException;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Accounts generateAccount(Accounts accounts) {
        if(accounts.getUsername().isEmpty() || accounts.getPassword().isEmpty())
            throw new InvalidAccountException("Invalid Account");
           Accounts existingAccount =  accountRepository.findByUsername(accounts.getUsername());
      if (existingAccount != null) {
          throw new InvalidAccountException("Account already exists");
      }
      Accounts newAccount = accountRepository.save(accounts);
      return newAccount;
    }

    @Override
    public List<Accounts> getAccounts() {
        List<Accounts> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Accounts deleteAccounts(Long accountId) {
        Accounts accounts = accountRepository.findById(accountId).orElseThrow(
                () -> new InvalidAccountException("Account not found")
        );
        accountRepository.delete(accounts);
        return accounts;
    }

    @Override
    public Accounts updateAccounts(Accounts accounts, String username, Long accountId) {
        Accounts existingAccount = accountRepository.findByAccountIdAndUsername(accountId, username);
        if (existingAccount == null) {
            throw new InvalidAccountException("Account not found to be updated");
        }
        accounts.setAccountId(existingAccount.getAccountId());
        Accounts updatedAccount = accountRepository.save(accounts);
        return updatedAccount;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        // First try to find by username
        Optional<Accounts> account = Optional.ofNullable(accountRepository.findByUsername(input));

        // If not found, try by email
        if (account.isEmpty()) {
            account = accountRepository.findByEmail(input);
        }

        // If still not found, throw error
        if (account.isEmpty()) {
            throw new InvalidAccountException("Account not found with email " + input);
        }

        return account.get();
    }

}
