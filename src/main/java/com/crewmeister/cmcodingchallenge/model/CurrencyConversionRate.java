package com.crewmeister.cmcodingchallenge.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CurrencyConversionRate {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currency;
    private double conversionRate;
    private LocalDate date;

    public CurrencyConversionRate(String currency, double conversionRate, LocalDate date) {
        this.currency = currency;
        this.conversionRate = conversionRate;
        this.date = date;
    }
}
