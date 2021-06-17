package edu.escuelaing.arsw.herokufirstwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Johann Cepeda
 */
public class ConcurrentTestWebApp {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean running = true;
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir en puerto:" + port);
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>" + "<html>"
                    + "<head>" + "<meta charset=\"UTF-8\">" + "<title>Title of the document</title>\n" + "</head>"
                    + "<body>" + "My Web Site" + "</body>" + "</html>" + inputLine;
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }

        return 36000; // returns default port if heroku-port isn't set (i.e. on localhost)
    }
}