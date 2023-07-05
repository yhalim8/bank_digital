package com.example.digital_banking.service;

import com.example.digital_banking.dtos.*;
import com.example.digital_banking.entities.*;
import com.example.digital_banking.enums.OpType;
import com.example.digital_banking.exceptions.BalanceNotSufficentException;
import com.example.digital_banking.exceptions.BankAccountNotFoundException;
import com.example.digital_banking.exceptions.CustomerNotFoundException;
import com.example.digital_banking.mappers.BankAccountMapper;
import com.example.digital_banking.repository.AccountOperationRepository;
import com.example.digital_banking.repository.BankAccountRepository;
import com.example.digital_banking.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional  //toutes les operations sont transactional
@AllArgsConstructor
@Slf4j  //pour logger les message
public class BankAccountServiceImplementation implements BankAccountService{
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CustomerRepository customerRepository;
    private BankAccountMapper dtoMapper;

    //Logger log= LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO); //transferer le dto vers l entité //recupirer les donnes from customerdto
        Customer savedcustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedcustomer);
    }

    @Override
    public CurrentAcountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if ((customer==null))
            throw new CustomerNotFoundException("Customer not found");
        CurrentAcount currentAcount=new CurrentAcount();
        currentAcount.setId(UUID.randomUUID().toString());
        currentAcount.setCustomer(customer);
        currentAcount.setDate(new Date());
        currentAcount.setOverDraft(overDraft);
        CurrentAcount savedBankAccount = bankAccountRepository.save(currentAcount);
        return dtoMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if ((customer==null))
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCustomer(customer);
        savingAccount.setDate(new Date());
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customers.stream().map(
                customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList()); //programation fonctionnel en utilisant les streams
        /* List<CustomerDTO> customerDTOList = new ArrayList<>();   //programmation imperative classique
        for (Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOList.add(customerDTO);
        }*/
        return customerDTOList;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAcount currentAcount = (CurrentAcount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAcount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount.getBalance()<amount)
            throw new BalanceNotSufficentException("Balance pas suffisant pour effectuer l'operation");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OpType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OpType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"transfert to"+accountIdDestination);
        credit(accountIdDestination,amount,"transfert from"+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount)
                return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
            else
                return dtoMapper.fromCurrentAccount((CurrentAcount) bankAccount);
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO); //transferer le dto vers l entité //recupirer les donnes from customerdto
        Customer savedcustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedcustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
         customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }
}
