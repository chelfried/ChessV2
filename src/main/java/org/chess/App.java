package org.chess;

import org.chess.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import java.io.IOException;

@EnableAutoConfiguration
public class App {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AppConfig.class,args);
    }

}

