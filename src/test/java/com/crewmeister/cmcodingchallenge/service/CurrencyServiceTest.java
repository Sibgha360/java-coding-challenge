package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.config.Messages;
import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CurrencyServiceTest {
    static PodamFactory factory = new PodamFactoryImpl();
    private static List<CurrencyConversionRate> listOfMockCurrencyConversionRate;
    private static CurrencyConversionRate mockCurrencyConversionRate;

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private CurrencyRepository currencyRepository;

    @BeforeAll
    static void init() {
        // create mock data
        listOfMockCurrencyConversionRate = factory.manufacturePojo(List.class, CurrencyConversionRate.class);
        mockCurrencyConversionRate = factory.manufacturePojo(CurrencyConversionRate.class);
    }

    @Test
    public void findAllEuFxRates_WhenRequested_ShouldReturnTheList() throws Exception {
        // ARRANGE
        doReturn(listOfMockCurrencyConversionRate).when(currencyRepository).findByCurrency(any());

        // ACT
        List<CurrencyConversionRate> allEuFxRates = currencyService.findAllEuFxRates();

        // ASSERT
        assertThat(allEuFxRates)
                .isNotEmpty()
                .hasSize(listOfMockCurrencyConversionRate.size())
                .isInstanceOf(List.class)
                .isSameAs(listOfMockCurrencyConversionRate);
    }

    @Test
    public void findAllEuFxRates_WhenCurrencyRatesAreNotAvailable_ShouldThrowResourceNotFoundException() {
        // ARRANGE
        doReturn(Collections.emptyList()).when(currencyRepository).findByCurrency(any());

        // ASSERT
        assertThatThrownBy(() -> {
            // ACT
            currencyService.findAllEuFxRates();
        }).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Messages.MESSAGE_NO_DATA);
    }

    @Test
    public void findEuFxRatesByDate_WhenRequested_ShouldReturnTheList() throws Exception {
        // ARRANGE
        doReturn(listOfMockCurrencyConversionRate).when(currencyRepository).findByDateAndCurrency(any(), any());

        // ACT
        List<CurrencyConversionRate> allEuFxRates = currencyService.findEuFxRateByDate(LocalDate.now());

        // ASSERT
        assertThat(allEuFxRates)
                .hasSize(listOfMockCurrencyConversionRate.size());

        assertThat(allEuFxRates.get(0))
                .isNotNull()
                .isInstanceOf(CurrencyConversionRate.class)
                .isEqualTo(listOfMockCurrencyConversionRate.get(0));
    }

    @Test
    public void findEuFxRateByDate_WhenCurrencyRatesAreNotAvailable_ShouldThrowResourceNotFoundException() {
        // ARRANGE
        doReturn(Collections.emptyList()).when(currencyRepository).findByDateAndCurrency(any(), any());

        // ASSERT
        assertThatThrownBy(() -> {
            // ACT
            currencyService.findEuFxRateByDate(LocalDate.now());
        }).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Messages.MESSAGE_NO_DATA);
    }

    @Test
    public void calculateAmountInEuroForGivenDate_WhenCurrencyExistsInRepository_ShouldReturnValidAmount() throws Exception {
        // ARRANGE
        doReturn(Arrays.asList(mockCurrencyConversionRate)).when(currencyRepository).findByDateAndCurrency(any(), any());
        doReturn(true).when(currencyRepository).existsByCurrency(any());

        // ACT
        Integer amount = 100;
        double amountInEuro = currencyService.calculateAmountInEuroForGivenDate(LocalDate.now(), "INR", amount);

        // ASSERT
        assertThat(amountInEuro)
                .isNotZero()
                .isEqualTo(amount / mockCurrencyConversionRate.getConversionRate());
    }

    @Test
    public void calculateAmountInEuroForGivenDate_WhenCurrencyDoesNotExistInRepository_ShouldReturnValidAmount() throws Exception {
        // ARRANGE
        doReturn(Arrays.asList(mockCurrencyConversionRate)).when(currencyRepository).findByDateAndCurrency(any(),any());
        doReturn(false).when(currencyRepository).existsByCurrency(any());

        // ACT
        Integer amount = 100;
        double amountInEuro = currencyService.calculateAmountInEuroForGivenDate(LocalDate.now(), "INR", amount);

        // ASSERT
        assertThat(amountInEuro)
                .isNotZero()
                .isEqualTo(amount / mockCurrencyConversionRate.getConversionRate());
    }

    @Test
    public void calculateAmountInEuroForGivenDate_WhenNoConversionRateIsFound_ShouldThrowResourceNotFoundException() throws Exception {
        // ARRANGE
        doReturn(Collections.emptyList()).when(currencyRepository).findByDateAndCurrency(any(),any());
        doReturn(false).when(currencyRepository).existsByCurrency(any());

        // ACT
        Integer amount = 100;

        // ASSERT
        assertThatThrownBy(() -> {
            // ACT
            currencyService.calculateAmountInEuroForGivenDate(LocalDate.now(), "INR", amount);
        }).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Messages.MESSAGE_NO_DATA);
    }

    @Test
    public void calculateAmountInEuroForGivenDate_WhenConversionRateIsZero_ShouldThrowException() throws Exception {
        // ARRANGE
        mockCurrencyConversionRate.setConversionRate(0d);
        doReturn(Arrays.asList(mockCurrencyConversionRate)).when(currencyRepository).findByDateAndCurrency(any(),any());
        doReturn(false).when(currencyRepository).existsByCurrency(any());
        Integer amount = 100;

        // ASSERT
        assertThatThrownBy(() -> {
            // ACT
            currencyService.calculateAmountInEuroForGivenDate(LocalDate.now(), "INR", amount);
        }).isInstanceOf(Exception.class)
                .hasMessage(Messages.MESSAGE_CORRUPT_DATA);
    }
}