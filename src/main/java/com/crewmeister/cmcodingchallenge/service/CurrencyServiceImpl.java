package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import com.crewmeister.cmcodingchallenge.config.SpringBootConfiguration;
import com.crewmeister.cmcodingchallenge.helper.Util;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    SpringBootConfiguration config;

    @Override
    public List<CurrencyConversionRate> findAllEuFxRates() throws ResourceNotFoundException {
        List<CurrencyConversionRate> byCurrency = currencyRepository.findByCurrency(Util.FX_CURRENCY);

        if (byCurrency.isEmpty()) {
            log.warn("No Data exists for Currency : {}", Util.FX_CURRENCY);
            throw new ResourceNotFoundException("There is no data for eu/fx currency rate. See Logs");
        }

        return byCurrency;
    }

    @Override
    public List<CurrencyConversionRate> findEuFxRateByDate(LocalDate date) throws ResourceNotFoundException {
        List<CurrencyConversionRate> byDateAndCurrency = currencyRepository.findByDateAndCurrency(date, Util.FX_CURRENCY);

        if (byDateAndCurrency.isEmpty()) {
            log.warn("No Data exists for Currency : {}", Util.FX_CURRENCY);
            throw new ResourceNotFoundException("There is no data for eu/fx currency rate. See Logs");
        }

        return byDateAndCurrency;
    }

    @Override
    public double calculateAmountInEuroForGivenDate(LocalDate date, String currency, Integer amount) throws Exception {
        //only executes ones for every currency. For rest of the requests the data is fetched from the repository
        if (!currencyRepository.existsByCurrency(currency)) {
            log.warn("Populating the currency rates for: {}", currency);
            Util.populateCurrencyRate(currencyRepository, currency, config.getBundesbankWebUrl());
        }

        List<CurrencyConversionRate> byDateAndCurrency = currencyRepository.findByDateAndCurrency(date, currency);

        if ( byDateAndCurrency.isEmpty()) {
            log.warn("No data exists. Currency: {}, Date: {}", currency, date);
            throw new ResourceNotFoundException("No data exists for given date and currency. See logs");
        }

        //there is just one index in the list
        CurrencyConversionRate currencyConversionRate = byDateAndCurrency.get(0);

        if (currencyConversionRate.getConversionRate() == 0d) {
            log.warn("Corrupt data. Conversion rate {}: Currency: {}, Date: {}", currencyConversionRate.getConversionRate(), currency, date);
            throw new Exception("Corrupt data for given currency and date. See logs");
        }

        //use the formula to convert one currency to Euro
        return amount / currencyConversionRate.getConversionRate();
    }
}
