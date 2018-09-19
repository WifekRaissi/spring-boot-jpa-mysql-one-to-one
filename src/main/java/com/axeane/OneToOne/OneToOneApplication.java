package com.axeane.OneToOne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.axeane.OneToOne")
@SpringBootApplication
public class OneToOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneToOneApplication.class, args);
    }
}
