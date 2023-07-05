package com.example.digital_banking.mappers;

import com.example.digital_banking.dtos.AccountOperationDTO;
import com.example.digital_banking.dtos.CurrentAcountDTO;
import com.example.digital_banking.dtos.CustomerDTO;
import com.example.digital_banking.dtos.SavingAccountDTO;
import com.example.digital_banking.entities.AccountOperation;
import com.example.digital_banking.entities.CurrentAcount;
import com.example.digital_banking.entities.Customer;
import com.example.digital_banking.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        //customerDTO.setName(customer.getName());
        //customerDTO.setId(customer.getId());
        //customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }
    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingAccountDTO);
        savingAccountDTO.setCustomer(fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType(SavingAccount.class.getSimpleName());
        return savingAccountDTO;
    }
    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingAccountDTO.getCustomer()));
        return savingAccount;
    }
    public CurrentAcount fromCurrentAccountDTO(CurrentAcountDTO currentAcountDTO){
        CurrentAcount currentAcount = new CurrentAcount();
        BeanUtils.copyProperties(currentAcountDTO,currentAcount);
        currentAcount.setCustomer(fromCustomerDTO(currentAcountDTO.getCustomer()));
        return currentAcount;
    }
    public CurrentAcountDTO fromCurrentAccount(CurrentAcount currentAcount){
        CurrentAcountDTO currentAcountDTO = new CurrentAcountDTO();
        BeanUtils.copyProperties(currentAcount,currentAcountDTO);
        currentAcountDTO.setCustomer(fromCustomer(currentAcount.getCustomer()));
        currentAcountDTO.setType(CurrentAcount.class.getSimpleName());
        return currentAcountDTO;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }
}
