package jeet.gaekwad.samplegnec_1.Controller.AccountsController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Service.AccountService.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Account Management APIs", description = "Endpoints for user account registration, retrieval, update, and deletion.")
@RestController
@RequestMapping("/v1")
public class AccountController {


    private AccountService accountService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }
    @Operation(
            summary = "Register a new Account",
            description = "This API is used to register a new user account. It accepts username, password, role, and email. Password is encrypted before saving."
    )
    @PostMapping("/register")
    public ResponseEntity<Accounts> createAccount(@RequestBody Accounts accounts) {
        accounts.setPassword(passwordEncoder.encode(accounts.getPassword()));
        Accounts newAccount =  accountService.generateAccount(accounts);
        return ResponseEntity.status(HttpStatus.OK).body(newAccount);
    }
    @Operation(
            summary = "Get all Accounts",
            description = "Retrieve a list of all registered user accounts. Only accessible to authorized users."
    )
    @GetMapping("/accounts")
    public ResponseEntity<List<Accounts>> getAllAccounts() {
        List<Accounts> allAccountList = accountService.getAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(allAccountList);
    }
    @Operation(
            summary = "Delete an Account by ID",
            description = "Delete a specific user account by providing its accountId."
    )
    @DeleteMapping("/deleteAccount/{accountId}")
    public ResponseEntity<Accounts> deleteAccount(@PathVariable Long accountId) {
        Accounts deletedAccount = accountService.deleteAccounts(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedAccount);
    }
    @Operation(
            summary = "Update an existing Account",
            description = "Update the details of an existing account by providing updated information along with username and accountId."
    )
    @PutMapping("/updateAccount/{username}/{accountId}")
    public ResponseEntity<Accounts> updateAccount(@RequestBody Accounts accounts, @PathVariable String username, @PathVariable Long accountId) {
        Accounts updatedAccount = accountService.updateAccounts(accounts, username,accountId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }
}
