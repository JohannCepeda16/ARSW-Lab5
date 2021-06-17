package edu.escuelaing.arsw.herokufirstwebapp;

import java.io.*;
import java.net.*;

public class HttpClient {

    public static void main(String[] args) throws IOException {
        URL pagina = new URL("http://localhost:35000/"+args[0]);
        URLConnection connection = pagina.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    connection.getInputStream()));
        in.close();

    }
}