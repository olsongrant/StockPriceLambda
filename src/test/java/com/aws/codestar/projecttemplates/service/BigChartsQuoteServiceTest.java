package com.aws.codestar.projecttemplates.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.List;

@DisplayName("Test for the BigChartsQuoteService")
public class BigChartsQuoteServiceTest {
    
    // A mock class for com.amazonaws.services.lambda.runtime.Context
    private final MockLambdaContext mockLambdaContext = new MockLambdaContext();
    
    private BigChartsQuoteService quoteService;
    
    private LambdaLogger logger;
    
    @BeforeEach
    public void setup() {
        this.logger = this.mockLambdaContext.getLogger();
        this.quoteService = new BigChartsQuoteService();
    }
    
    @Test
    @DisplayName("Basic test for quote service")
    void testBasicQuotePull() {
        
        Map<String, Float> priceMap = this.quoteService.pricesForSymbols(List.of("IBM", "QQQ"), this.logger);
        assertEquals(priceMap.size(), 2);
    }
}