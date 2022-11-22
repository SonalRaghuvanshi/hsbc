package io.muserver.docs.samples;

import static io.muserver.MuServerBuilder.httpServer;

import java.io.IOException;
import java.util.Map;

import io.muserver.Method;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.MuServer;
import io.muserver.RouteHandler;
import io.muserver.handlers.ResourceHandlerBuilder;

public class DetailsCustomer {
    public static void main(String[] args) {
        MuServer server = httpServer()
            .addHandler(Method.POST, "/upload", new CustomerRequestBodyHandler())
            .addHandler(ResourceHandlerBuilder.fileOrClasspath("src/main/resources/samples", "/samples"))
            .start();
        System.out.println("Upload example started at " + server.uri().resolve("/upload.html"));
    }
    
    private static class CustomerRequestBodyHandler implements RouteHandler {
        @Override
        public void handle(MuRequest request, MuResponse response, Map<String,String> pathParams) throws IOException {

            // Returns null if there is no parameter with that value
           String dateEntered = request.form().get("dateEntered");

            // You can loop through all the form data
           response.sendChunk("\n\n Booking Details");
           try {
           SaveRestaurant data = (SaveRestaurant) ResourceManager.load("1.save");
           response.sendChunk('\n' + "Customer Name: " + data.customerName + "Table Size: " + data.tableSize + "Date: " + data.date + "Time: " + data.time);
           }catch (Exception e) {
        	   System.out.println("Couldn't load save data: " +e.getMessage());
           }
        }
    }
}
