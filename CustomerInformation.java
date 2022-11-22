package io.muserver.docs.samples;

import static io.muserver.MuServerBuilder.httpServer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.muserver.Method;
import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.MuServer;
import io.muserver.RouteHandler;
import io.muserver.handlers.ResourceHandlerBuilder;

// for uploading a file
public class CustomerInformation {

    public static void main(String[] args) {
        MuServer server = httpServer()
            .addHandler(Method.POST, "/upload", new AsyncRequestBodyHandler())
            .addHandler(ResourceHandlerBuilder.classpathHandler("/samples"))
            .start();

        System.out.println("Upload a file at " + server.uri().resolve("/async-upload.html"));

    }

    private static class AsyncRequestBodyHandler implements RouteHandler {
        @Override
        public void handle(MuRequest request, MuResponse response, Map<String,String> pathParams) throws IOException {

            // Returns null if there is no parameter with that value
            String customerName = request.form().get("customerName");

            // Specifying a default value:
            String tableSize = request.form().get("tableSize");

            // Gets a number, or returns the default value if it's missing or not a number.
            // There are also getFloat, getDouble, getLong and getBoolean methods
            String date = request.form().get("date");
            String time = request.form().get("time");
            response.sendChunk(customerName + "\n" + tableSize + "\n" + date + "\n" + time);

            // You can loop through all the form data
            SaveRestaurant data = new SaveRestaurant();
            data.customerName = customerName;
            data.tableSize = tableSize;
            data.date = date;
            data.time = time;
            try {
            	ResourceManager.save(data, "1.save");
            }catch (Exception e) {
            	System.out.println("Couldn't save: "+ e.getMessage());
            }
            response.sendChunk("\n\n Received your order");
            for (Map.Entry<String, List<String>> entry : request.form().all().entrySet()) {
                response.sendChunk('\n' + entry.getKey() + " = " + entry.getValue());
            }
        }
    }
}
