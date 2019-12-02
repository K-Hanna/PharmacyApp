package Pharmacy.controller;

import Pharmacy.type.Receipt;
import Pharmacy.storage.ReceiptStorage;
import Pharmacy.storage.ReceiptStorageImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status.*;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class ReceiptController {

    private final static String RECEIPT_ID_PARAM_NAME = "receiptId";
    private ReceiptStorage receiptStorage = new ReceiptStorageImpl();

    public Response serveGetReceiptRequest(IHTTPSession session){

        Map<String, List<String>> requestParameters = session.getParameters();
        if(requestParameters.containsKey(RECEIPT_ID_PARAM_NAME)){
            List<String> receiptIdParams = requestParameters.get(RECEIPT_ID_PARAM_NAME);
            String receiptIdParam = receiptIdParams.get(0);
            long receiptId = 0;

            try{
                receiptId = Long.parseLong(receiptIdParam);
            } catch (NumberFormatException nfe){
                System.err.println("Error " + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Incorrect request.");
            }

            Receipt receipt = receiptStorage.getReceipt(receiptId);
            if(receipt != null){
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(receipt);
                    return newFixedLengthResponse(OK, "text/plain", response);
                } catch (JsonProcessingException e){
                    System.err.println("Error: " + e);
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error.");
                }
            }
            return newFixedLengthResponse(NOT_FOUND, "application/json", "Receipt not found.");
        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Incorrect request.");
    }

    public Response serveGetReceiptsRequest(IHTTPSession session){

        ObjectMapper objectMapper = new ObjectMapper();
        String response = "";

        try{
            response = objectMapper.writeValueAsString(receiptStorage.getAllReceipts());
        } catch (JsonProcessingException e) {
            System.out.println("Error " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error.");
        }
        return newFixedLengthResponse(OK, "application/json", response);
    }

    public Response serveAddReceiptRequest(IHTTPSession session){

        ObjectMapper objectMapper = new ObjectMapper();
        long randomReceipId = System.currentTimeMillis();

        String lengthHeader = session.getHeaders().get("content-length");
        int contentLength = Integer.parseInt(lengthHeader);
        byte[] buffer = new byte[contentLength];

        try{
            session.getInputStream().read(buffer, 0, contentLength);
            String requestBody = new String(buffer).trim();
            Receipt requestReceipt = objectMapper.readValue(requestBody, Receipt.class);
            requestReceipt.setId(randomReceipId);

            receiptStorage.addReceipt(requestReceipt);
        } catch (Exception e){
            System.err.println("Error " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error.");
        }
        return newFixedLengthResponse(OK, "text/plain", "Receipt added with id: " + randomReceipId);
    }

    public Response serveRemoveReceiptRequest(IHTTPSession session){

        Map<String, List<String>> requestParameters = session.getParameters();
        if(requestParameters.containsKey(RECEIPT_ID_PARAM_NAME)){
            List<String> receiptIdParams = requestParameters.get(RECEIPT_ID_PARAM_NAME);
            String receiptIdParam = receiptIdParams.get(0);
            long receiptId = 0;

            try{
                receiptId = Long.parseLong(receiptIdParam);
            } catch (NumberFormatException nfe){
                System.err.println("Error " + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Incorrect request.");
            }

            Receipt receipt = receiptStorage.getReceipt(receiptId);

            if(receipt != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(receipt);
                    receiptStorage.removeReceipt(receiptId);
                    return newFixedLengthResponse(OK, "text/plain", "Receipt removed.");
                } catch (JsonProcessingException e) {
                    System.err.println("Error: " + e);
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error.");
                }
            }

            return newFixedLengthResponse(NOT_FOUND, "application/json", "Receipt not found.");
        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Incorrect request.");
    }

}
