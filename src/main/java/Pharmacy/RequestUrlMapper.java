package Pharmacy;

import Pharmacy.controller.ReceiptController;
import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Method.DELETE;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

class RequestUrlMapper {

    private final static String ADD_RECEIPT_URL = "/receipt/add";
    private final static String GET_RECEIPT_URL = "/receipt/get";
    private final static String GET_ALL_RECEIPT_URL = "/receipt/getAll";
    private final static String DELETE_RECEIPT_URL = "/receipt/delete";

    private ReceiptController receiptController = new ReceiptController();

    Response delegateRequest(IHTTPSession session){

        if(GET.equals(session.getMethod()) && GET_RECEIPT_URL.equals(session.getUri())){
            return receiptController.serveGetReceiptRequest(session);
        } else if (GET.equals(session.getMethod()) && GET_ALL_RECEIPT_URL.equals(session.getUri())){
            return receiptController.serveGetReceiptsRequest(session);
        } else if (POST.equals(session.getMethod()) && ADD_RECEIPT_URL.equals(session.getUri())){
            return receiptController.serveAddReceiptRequest(session);
        } else if (DELETE.equals(session.getMethod()) && DELETE_RECEIPT_URL.equals(session.getUri())) {
            return receiptController.serveRemoveReceiptRequest(session);
        }

        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, "text/plain", "not found");
    }
}
