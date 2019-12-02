package Pharmacy;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class PharmacyApp extends NanoHTTPD {

    RequestUrlMapper requestUrlMapper = new RequestUrlMapper();

    public PharmacyApp(int port) throws IOException{
        super((port));
        start(5000, false);
        System.out.println("Server has been started.");
    }

    public static void main(String[] args) {
        try {
            new PharmacyApp(8080);
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public Response serve(IHTTPSession session){
        return requestUrlMapper.delegateRequest(session);
    }
}
