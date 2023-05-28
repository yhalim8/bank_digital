package com.example.digital_banking.entities;

import com.example.digital_banking.enums.OpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private Date date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OpType type;
    @ManyToOne
    private BankAccount bankAccount;
}
