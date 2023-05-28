package com.example.digital_banking.entities;

import com.example.digital_banking.enums.AccStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4,discriminatorType = DiscriminatorType.STRING)
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private Date date;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccStatus status;
    private String currency;
    @ManyToOne
    private Customer customer;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bankAccount")
    private List<AccountOperation> operations;
}
