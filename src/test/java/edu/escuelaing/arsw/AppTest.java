package edu.escuelaing.arsw;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import edu.escuelaing.arsw.herokufirstwebapp.HttpClient;
import edu.escuelaing.arsw.herokufirstwebapp.HttpServer;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void RecibePeticionesConcurrentes() {
        String[] args = { "index.html" };
        ExecutorService pool = Executors.newFixedThreadPool(102);
        pool.submit(() -> {
            try {
                HttpServer.main(args);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        for (int i = 0; i < 100; i++) {
            pool.submit(() -> {
                try {
                    HttpClient.main(args);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    assertFalse(false);
                }
            });
        }
        args[0] = "bye";
        pool.submit(() -> {
            try {
                HttpClient.main(args);
            } catch (IOException ex) {
                ex.printStackTrace();
                assertFalse(false);
            }
        });
        pool.shutdown();
        assertTrue(true);
    }
}
