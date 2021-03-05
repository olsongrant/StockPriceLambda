package com.aws.codestar.projecttemplates.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class BigChartsQuoteService {
    
	private static final String BASE_URL_STRING = 
		"https://bigcharts.marketwatch.com/quotes/multi.asp?view=q&msymb=";

    public BigChartsQuoteService() {
        super();
    }
    
   	protected Document documentForSymbol(String symbolList, LambdaLogger logger) {
   	    logger.log("BigChartsQuoteService::documentForSymbol entered");
		String url = BigChartsQuoteService.BASE_URL_STRING + symbolList;
		Document doc = null;
		try {
		    logger.log("About to invoke URL " + url);
			doc = Jsoup.connect(url).get();
			logger.log("Title: " + doc.title());
		} catch (IOException e) {
            logger.log(e.getMessage());
			e.printStackTrace();
		}
		logger.log("exiting BigChartsQuoteService::documentForSymbol");
		return doc;
	}
	
	public Map<String, Float> pricesForSymbols(List<String> aSymbolList, LambdaLogger logger) {
	    logger.log("entering BigChartsQuoteService::pricesForSymbols");
		HashMap<String, Float> priceMap = new HashMap<>();
		List<String> symbols = List.copyOf(aSymbolList);
		String joinedSymbols = symbols.stream().collect(Collectors.joining("+"));
		logger.log("joined symbol list: " + joinedSymbols);
		Document doc = this.documentForSymbol(joinedSymbols, logger);
		Elements elements = doc.getElementsByClass("multiquote quick");
		if (elements.size() < 1) { 
			logger.log("no 'multiquote quick' elements");
			return null;
		}		
		Element ourTable = elements.first();
		Elements tbodies = ourTable.getElementsByTag("tbody");
		if (tbodies.size() < 1) {
			logger.log("no tbody elements!");
			return null;
		}
		Element tbody = tbodies.first();
		Elements tableRows = tbody.getElementsByTag("tr");
		Iterator<Element> trElements = tableRows.iterator();
		while (trElements.hasNext()) {
			Element trElement = trElements.next();
			Elements symbColElements = trElement.getElementsByClass("symb-col");
			if (symbColElements.size() < 1) {
				logger.log("looked for symb-col class element in a tr element, but did not find.");
				continue;
			}
			Element tdSymbolElement = symbColElements.first();
			String symbol = tdSymbolElement.text();
			Elements lastColElements = trElement.getElementsByClass("last-col");
			if (lastColElements.size() < 1) {
				logger.log("looked for last-col class element in a tr element, but did not find.");
				continue;
			}
			Element lastColElement = lastColElements.first();
			String lastPriceString = lastColElement.text();
			lastPriceString = lastPriceString.replaceAll(",", "");
			Float lastPrice = Float.valueOf(lastPriceString);
			priceMap.put(symbol, lastPrice);
		}
		logger.log("Exiting BigChartsQuoteService::pricesForSymbols");
		return priceMap;
	}
}