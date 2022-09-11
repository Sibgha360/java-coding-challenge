package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import com.crewmeister.cmcodingchallenge.helper.Util;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.*;

@RestController()
@RequestMapping("/api")
@Slf4j
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @GetMapping("/currencies")
    public ResponseEntity<Set<String>> getCurrencies() {
        Map<String, String> availableCurrencies = Util.getAvailableCurrencies();
        return new ResponseEntity<Set<String>>(availableCurrencies.keySet(), HttpStatus.OK);
    }

    @GetMapping("/euFxRates")
    public ResponseEntity<List<CurrencyConversionRate>> getEuFxRates() throws ResourceNotFoundException {
        List<CurrencyConversionRate> allEuFxRates = currencyService.findAllEuFxRates();
        return new ResponseEntity<List<CurrencyConversionRate>>(allEuFxRates, HttpStatus.OK);
    }

    @GetMapping("/euFxRateByDay")
    public ResponseEntity<List<CurrencyConversionRate>> getEuFxRatebyDay(@PathParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ResourceNotFoundException {
        List<CurrencyConversionRate> euFxRateByDate = currencyService.findEuFxRateByDate(date);
        return new ResponseEntity<List<CurrencyConversionRate>>(euFxRateByDate, HttpStatus.OK);
    }

    @GetMapping("/amountInEuroByDay")
    public ResponseEntity<Double> getAmountInEuroByDay(@PathParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathParam("currency") String currency, @PathParam("amount") Integer amount) throws Exception {
        double amountInEuro = currencyService.calculateAmountInEuroForGivenDate(date, currency, amount);

        //return the amount here
        return new ResponseEntity<Double>(amountInEuro, HttpStatus.OK);
    }
}
