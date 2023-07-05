package com.example.digital_banking;

import com.example.digital_banking.dtos.CurrentAcountDTO;
import com.example.digital_banking.dtos.CustomerDTO;
import com.example.digital_banking.dtos.SavingAccountDTO;
import com.example.digital_banking.entities.*;
import com.example.digital_banking.enums.AccStatus;
import com.example.digital_banking.enums.OpType;
import com.example.digital_banking.exceptions.BalanceNotSufficentException;
import com.example.digital_banking.exceptions.BankAccountNotFoundException;
import com.example.digital_banking.exceptions.CustomerNotFoundException;
import com.example.digital_banking.repository.AccountOperationRepository;
import com.example.digital_banking.repository.BankAccountRepository;
import com.example.digital_banking.repository.CustomerRepository;
import com.example.digital_banking.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@AllArgsConstructor
public class DigitalBankingApplication  {
  //  private CustomerRepository customerRepository;
  //  private BankAccountRepository bankAccountRepository;
  //  private AccountOperationRepository accountOperationRepository;

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("halim","khalid","yassine").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);

            });
            bankAccountService.listCustomers().forEach(customer ->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*12000,9.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            bankAccountService.bankAccountList().forEach(account -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        String accountId;
                        if (account instanceof SavingAccountDTO){
                            accountId=((SavingAccountDTO) account).getId();}
                        else {
                            accountId = ((CurrentAcountDTO) account).getId();
                        }
                        bankAccountService.credit(accountId, 10000+Math.random()*12000,"credit");
                        bankAccountService.debit(accountId, 10000+Math.random()*9000,"debit");
                    } catch (BankAccountNotFoundException | BalanceNotSufficentException e) {
                        e.printStackTrace();
                    }
                }
            });
        };
    }
    }

    /*@Override
    public void run(String... args) throws Exception {
        //Dao test
        Stream.of("halim","khalid","yassine").forEach(name -> {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(name+"@gmail.com");
            customerRepository.save(customer);

        });
        customerRepository.findAll().forEach(cust ->{
            CurrentAcount currentAcount = new CurrentAcount();
            currentAcount.setId(UUID.randomUUID().toString());
            currentAcount.setBalance(Math.random()*90000);
            currentAcount.setDate(new Date());
            currentAcount.setStatus(AccStatus.ACTIVATED);
            currentAcount.setCustomer(cust);
            currentAcount.setCurrency("DH");
            currentAcount.setOverDraft(6000);
            bankAccountRepository.save(currentAcount);
            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setBalance(Math.random()*90000);
            savingAccount.setDate(new Date());
            savingAccount.setStatus(AccStatus.ACTIVATED);
            savingAccount.setCustomer(cust);
            savingAccount.setCurrency("DH");
            savingAccount.setInterestRate(6000);
            bankAccountRepository.save(savingAccount);
        });
        bankAccountRepository.findAll().forEach(bankAccount ->{
            for (int i=0;i<6;i++){
            AccountOperation accountOperation = new AccountOperation();
            accountOperation.setDate(new Date());
            accountOperation.setAmount(Math.random()*12000);
            accountOperation.setType(Math.random()>0.5? OpType.DEBIT:OpType.CREDIT);
            accountOperation.setBankAccount(bankAccount);
            accountOperationRepository.save(accountOperation);
            }
        });
    }*/

