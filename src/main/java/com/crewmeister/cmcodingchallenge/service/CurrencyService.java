package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyService {
    List<CurrencyConversionRate> findAllEuFxRates() throws ResourceNotFoundException;
    List<CurrencyConversionRate> findEuFxRateByDate(LocalDate date) throws ResourceNotFoundException;
    double calculateAmountInEuroForGivenDate(LocalDate date, String currency, Integer amount) throws Exception;
}
