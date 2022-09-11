package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyConversionRate, Long> {
    List<CurrencyConversionRate> findByCurrency(String currency);
    List<CurrencyConversionRate> findByDateAndCurrency(LocalDate date, String currency);
    boolean existsByCurrency(String currency);
}
