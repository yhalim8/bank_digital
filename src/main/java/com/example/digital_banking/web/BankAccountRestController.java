package com.example.digital_banking.web;

import com.example.digital_banking.dtos.AccountOperationDTO;
import com.example.digital_banking.dtos.BankAccountDTO;
import com.example.digital_banking.dtos.CustomerDTO;
import com.example.digital_banking.exceptions.BankAccountNotFoundException;
import com.example.digital_banking.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> accounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(String accountId){
        return bankAccountService.accountHistory(accountId);
    }
}
