package jeet.gaekwad.samplegnec_1.Service.AccountService;

import jeet.gaekwad.samplegnec_1.Model.Accounts;

import java.util.List;

public interface AccountService {
    Accounts generateAccount(Accounts accounts);
    List<Accounts> getAccounts();
    Accounts deleteAccounts(Long accountId);
    Accounts updateAccounts(Accounts accounts, String username, Long accountId);
}
