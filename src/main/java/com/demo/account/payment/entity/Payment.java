package com.demo.account.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="payment_seq")
    @SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize=1)
    @JsonIgnore
    private int id;
    @NonNull
    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch=FetchType.LAZY
    )
    @JoinColumn(
            name = "fromAccount_id",
            referencedColumnName = "id"
    )
    private Account fromAccount;

    @NonNull
    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch=FetchType.LAZY
    )
    @JoinColumn(
            name = "toAccount_id",
            referencedColumnName = "id"
    )
    private Account toAccount;

    @NonNull
    private BigDecimal amount;
    private Date lastUpdate;
    private String referenceNo;
}
