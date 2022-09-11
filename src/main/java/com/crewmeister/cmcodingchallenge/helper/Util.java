package com.crewmeister.cmcodingchallenge.helper;

import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class Util {
	public static String FX_CURRENCY = "USD";
	public static String DATE_FORMAT_BUNDESBANK_DATA = "yyyy-MM-dd";
	public static String CSV_SPLITTER_BUNDESBANK_DATA = ";";
	public static String CSV_LINE_SPLITTER_BUNDESBANK_DATA = "\r\n";

	//the date/currency data starts after the defined line number
	public static int CSV_DATA_OFFSET = 11;

	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_BUNDESBANK_DATA)
			.withLocale(Locale.GERMANY);

    public static Map<String, String> getAvailableCurrencies(){
		Map<String, String> currencytoFlowRefContextMap = new HashMap<>();

		currencytoFlowRefContextMap.put("AUD","BBEX3/D.AUD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("BGN","BBEX3/D.BGN.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("ZAR","BBEX3/D.ZAR.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("BRL","BBEX3/D.BRL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("CAD","BBEX3/D.CAD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("CHF","BBEX3/D.CHF.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("CNY","BBEX3/D.CNY.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("CYP","BBEX3/D.CYP.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("CZK","BBEX3/D.CZK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("DKK","BBEX3/D.DKK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("EEK","BBEX3/D.EEK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("GBP","BBEX3/D.GBP.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("GRD","BBEX3/D.GRD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("HKD","BBEX3/D.HKD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("HRK","BBEX3/D.HRK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("HUF","BBEX3/D.HUF.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("IDR","BBEX3/D.IDR.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("ILS","BBEX3/D.ILS.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("INR","BBEX3/D.INR.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("ISK","BBEX3/D.ISK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("JPY","BBEX3/D.JPY.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("KRW","BBEX3/D.KRW.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("LTL","BBEX3/D.LTL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("LVL","BBEX3/D.LVL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("MTL","BBEX3/D.MTL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("MXN","BBEX3/D.MXN.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("MYR","BBEX3/D.MYR.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("NOK","BBEX3/D.NOK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("NZD","BBEX3/D.NZD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("PHP","BBEX3/D.PHP.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("PLN","BBEX3/D.PLN.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("ROL","BBEX3/D.ROL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("RON","BBEX3/D.RON.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("RUB","BBEX3/D.RUB.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("SEK","BBEX3/D.SEK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("SGD","BBEX3/D.SGD.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("SIT","BBEX3/D.SIT.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("SKK","BBEX3/D.SKK.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("THB","BBEX3/D.THB.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("TRL","BBEX3/D.TRL.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("TRY","BBEX3/D.TRY.EUR.BB.AC.000");
		currencytoFlowRefContextMap.put("USD","BBEX3/D.USD.EUR.BB.AC.000");

		return currencytoFlowRefContextMap;
    }

	@NotNull
	public static String getHttpResponseInCsv(String url) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();

		Request request = new Request.Builder()
				.url(url)
				.addHeader("Accept", "text/csv")
				.build();
		Response response = client.newCall(request).execute();

		StringBuilder sb = new StringBuilder();
		for (int ch; (ch = response.body().byteStream().read()) != -1; ) {
			sb.append((char) ch);
		}

		return sb.toString();
	}

	@NotNull
	public static List<String> parseCsvToList(String s) throws IOException {
		StringReader stringReader = new StringReader(s);
		List<String> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(stringReader)) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(CSV_LINE_SPLITTER_BUNDESBANK_DATA);
				records.add(values[0]);
			}
		}
		return records;
	}

	@NotNull
	public static ArrayList<CurrencyConversionRate> getCurrencyConversionRates(List<String> records, String currency) throws ParseException {
		ArrayList<CurrencyConversionRate> currencyConversionRates = new ArrayList<>();

		for (int n = CSV_DATA_OFFSET; n < records.size(); n++) {
			String[] x = records.get(n).split(CSV_SPLITTER_BUNDESBANK_DATA);
			if (x[1].equals(".")) {
				continue;
			}

			NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);

			double conversionRate = nf.parse(x[1]).doubleValue();
			String dateString = x[0];
			LocalDate date = LocalDate.parse(dateString, formatter);

			currencyConversionRates.add(new CurrencyConversionRate(currency, conversionRate, date));
		}
		return currencyConversionRates;
	}

	public static void populateCurrencyRate(CurrencyRepository currencyRepository, String currency, String url) throws IOException, ParseException {
		log.info("Populating repository with currency conversion rates. Currency: "+ currency);
		String flowRefKeyContext = Util.getAvailableCurrencies().get(currency);

		String dataInCsv = getHttpResponseInCsv(url + "/" + flowRefKeyContext);
		List<String> dataInList = parseCsvToList(dataInCsv);
		ArrayList<CurrencyConversionRate> currencyConversionRates = getCurrencyConversionRates(dataInList, currency);
		currencyRepository.saveAll(currencyConversionRates);
		log.info("Repository populated. Currency: "+ currency);
	}
}
