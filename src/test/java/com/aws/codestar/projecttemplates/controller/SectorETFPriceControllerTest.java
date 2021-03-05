package com.aws.codestar.projecttemplates.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.List;

import org.json.JSONObject;

@DisplayName("Test for the SectorETFPriceController")
public class SectorETFPriceControllerTest {
    
    // A mock class for com.amazonaws.services.lambda.runtime.Context
    private final MockLambdaContext mockLambdaContext = new MockLambdaContext();
    
    private SectorETFPriceController controller;
    
    private LambdaLogger logger;
    
    @BeforeEach
    public void setup() {
        this.logger = this.mockLambdaContext.getLogger();
        this.controller = new SectorETFPriceController();
        
    }
    
    @Test
    @DisplayName("Basic test for the sector ETF controller")
    void testSectorETFPull() {
        JSONObject pricesJSON = this.controller.getSectorPrices(this.logger); 
        System.out.println(pricesJSON);
        assertEquals(pricesJSON.length(), 11);
    }
}