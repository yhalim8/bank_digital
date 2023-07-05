package com.example.digital_banking.dtos;

import com.example.digital_banking.entities.BankAccount;
import com.example.digital_banking.enums.OpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class AccountOperationDTO {
    private long id;
    private Date date;
    private double amount;
    private OpType type;
    private String description;
}
