package org.sipr;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan({"org.sipr.*"})
public class SipRServer {

    public static void main(String[] args) {
        run(SipRServer.class, args);
    }
}
