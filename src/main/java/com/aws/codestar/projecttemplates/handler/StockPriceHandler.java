package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.aws.codestar.projecttemplates.GatewayResponse;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.net.URL;
import java.net.HttpURLConnection;

import com.aws.codestar.projecttemplates.controller.SectorETFPriceController;

/**
 * Handler for requests to Lambda function.
 */
public class StockPriceHandler implements RequestHandler<Object, Object> {
    
    private static SectorETFPriceController controller = new SectorETFPriceController();
 

    public Object handleRequest(final Object input, final Context context) {
        Map<String, String> event = (Map<String, String>) input;
        LambdaLogger logger = context.getLogger();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
            // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + new JSONObject(System.getenv()));
        logger.log("CONTEXT: " + new JSONObject(context));
        // process event
        JSONObject contentObject = new JSONObject();
        JSONObject eventJSON = this.inspectEventContents(event, logger);
//        contentObject.put("EVENT-TOSTRING", eventJSON);
        logger.log("EVENT: " + eventJSON);
//        contentObject.put("EVENT-CLASS", event.getClass().toString());
        logger.log("EVENT TYPE: " + event.getClass().toString());
        if (!this.checkOutgoingConnectivity(logger)) {
            JSONObject connectionProblem = new JSONObject();
            connectionProblem.put("OUTGOING_CONNECTION_TEST", "Failed");
            return new GatewayResponse(connectionProblem.toString(), headers, 200);
        }
        JSONObject pricesObject = StockPriceHandler.controller.getSectorPrices(logger);
//        contentObject.put("PRICES", pricesObject);
        return new GatewayResponse(pricesObject.toString(), headers, 200);
    }
    
    private JSONObject inspectEventContents(Map<String, String> contentsMap, LambdaLogger logger) {
        JSONObject viewables = new JSONObject();
        for (String aKey: contentsMap.keySet()) {
            try {
                Object val = contentsMap.get(aKey);
                logger.log("Event key: " + aKey + " value: " + val);
                viewables.put(aKey, val);
            } catch (Exception e) {
                logger.log("Exception when looking at map key " + aKey);
                logger.log(e.getMessage());
                System.out.println(e);
            }
        }
        return viewables;
    }
    
    private boolean checkOutgoingConnectivity(LambdaLogger logger) {
        try {
            URL urlObj = new URL("https://jsonplaceholder.typicode.com/users");
            HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
            int responseCode = httpCon.getResponseCode();
            logger.log("response code from URL check in checkOutgoingConnectivity: " + responseCode);
            return (responseCode > 0) ? true : false;
        } catch (Exception e) {
            logger.log(e.getMessage());
            return false;
        }
    }
}
