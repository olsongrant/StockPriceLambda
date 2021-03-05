package com.aws.codestar.projecttemplates.controller;

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

import com.aws.codestar.projecttemplates.service.BigChartsQuoteService;

public class SectorETFPriceController {
    
    private BigChartsQuoteService quoteService;
		
	private static final List<String> SYMBOL_LIST = List.of("XLY", "XLP", "XLE", "XLF", "XLV", "XLI", "XLB", "XLK", "XLU", "XLRE", "XLC");
	
	public SectorETFPriceController() {
	    super();
	    this.quoteService = new BigChartsQuoteService();
	}
	
	public JSONObject getSectorPrices(LambdaLogger logger) {
        Map<String, Float> priceMap = this.quoteService.pricesForSymbols(SYMBOL_LIST, logger);
        JSONObject pricesJSON = new JSONObject();
        for (String key: priceMap.keySet()) {
            try {
                Float price = priceMap.get(key);
                pricesJSON.put(key, price);
            } catch (Exception e) {
                logger.log(e.getMessage());
            }
            
        }
        return pricesJSON;
	}
	
	
      
}