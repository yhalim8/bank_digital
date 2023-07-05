package com.example.digital_banking.dtos;

import com.example.digital_banking.enums.AccStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentAcountDTO extends BankAccountDTO {
    private String id;
    private Date date;
    private double balance;
    private AccStatus status;
    private String currency;
    private CustomerDTO customer;
    private double overDraft;
}
