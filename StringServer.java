import java.io.IOException;
import java.net.URI;

import java.util.ArrayList;

// URLHandler in "Server.java"
class Handler implements URLHandler {

    // Stores "single string"
    // Chose to use an ArrayList to store the strings being added
    ArrayList<String> singleString = new ArrayList<String>();

    // Allows me to return a string in the next method
    public String printArray() {
        String finalString = "";
        for (int i = 0; i < singleString.size(); i++) {
            // Adds line break
            finalString += singleString.get(i)+"\n";
        }
        return finalString;
    }

    public String handleRequest(URI url) {


        // No query, simply display the ArrayList (previous inputs)
        if (url.getPath().equals("/")) {
            String emptyRequest = printArray();
            return emptyRequest;
        }

        else {
            System.out.println("Path: " + url.getPath());

            // Correct request
            if (url.getPath().contains("/add-message")) {

                String[] parameters = url.getQuery().split("=");

                if (parameters[0].equals("s")) {

                    for (int i = 1; i < parameters.length; i++) {
                        // Everything after the "="
                        singleString.add(parameters[i]);
                    }

                    String printedArray = printArray();
                    return printedArray;
                }
            }
            // Invalid request
            return "404 Not Found";
        }
    }
}

class StringServer extends Handler {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between "
            + "1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server.start(port, new StringServer());

    }
}



    
