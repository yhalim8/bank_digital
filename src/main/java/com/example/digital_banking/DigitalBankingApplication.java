package com.example.digital_banking;

import com.example.digital_banking.entities.*;
import com.example.digital_banking.enums.AccStatus;
import com.example.digital_banking.enums.OpType;
import com.example.digital_banking.repository.AccountOperationRepository;
import com.example.digital_banking.repository.BankAccountRepository;
import com.example.digital_banking.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@AllArgsConstructor
public class DigitalBankingApplication implements CommandLineRunner {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Override
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
    }
}
