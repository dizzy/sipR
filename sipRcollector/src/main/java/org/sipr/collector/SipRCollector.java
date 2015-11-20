package org.sipr.collector;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@ComponentScan({"org.sipr.collector", "org.sipr.utils"})
public class SipRCollector {

    public static void main(String[] args) {
        run(SipRCollector.class, args);
    }
}
