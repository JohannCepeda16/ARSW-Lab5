package edu.escuelaing.arsw.herokufirstwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseProcesor implements Runnable {

    Socket socket;

    public ResponseProcesor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine, outputLine;
            String method = "", path = "", version = "";
            while ((inputLine = in.readLine()) != null) {
                if (method.isEmpty()) {
                    String[] requestInfo = inputLine.split(" ");
                    method = requestInfo[0];
                    path = requestInfo[1];
                    version = requestInfo[2];
                }

                System.out.println(inputLine);

                if (!in.ready()) {
                    break;
                }
                outputLine = getResponse(path);
                out.println(outputLine);
                out.close();
                in.close();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Retorna codigo html para la pagina
     * 
     * @return
     */
    public String getResponse(String path) {
        String type = "text/html";
        if (path.endsWith(".css")) {
            type = "text/css";
        } else if (path.endsWith(".js")) {
            type = "text/javascript";
        }

        Path file = Paths.get("./www" + path);
        Charset charset = Charset.forName("UTF-8");
        String outMsg = "";
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                outMsg += "\r\n" + line;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return "HTTP/1.1 200 OK\r\n" + "Content-Type: " + type + "\r\n" + "\r\n" + "<!DOCTYPE html>" + outMsg;
    }

}
