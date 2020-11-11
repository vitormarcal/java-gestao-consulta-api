package br.com.sismedicina.gestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestorApplication.class, args);
    }


}
