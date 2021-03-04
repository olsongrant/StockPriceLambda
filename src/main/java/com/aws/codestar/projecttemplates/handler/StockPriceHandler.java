package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.aws.codestar.projecttemplates.GatewayResponse;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class StockPriceHandler implements RequestHandler<Map<String, String>, GatewayResponse> {
 

    public GatewayResponse handleRequest(final Map<String, String> event, final Context context) {
        LambdaLogger logger = context.getLogger();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
            // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + new JSONObject(System.getenv()));
        logger.log("CONTEXT: " + new JSONObject(context));
        // process event
        JSONObject contentObject = new JSONObject();
        contentObject.put("EVENT-TOSTRING", new JSONObject(event));
        logger.log("EVENT: " + new JSONObject(event));
        contentObject.put("EVENT-CLASS", event.getClass().toString());
        logger.log("EVENT TYPE: " + event.getClass().toString());
        contentObject.put("Message", "Lovely Peasants!");
        return new GatewayResponse(contentObject.toString(), headers, 200);
    }
}
