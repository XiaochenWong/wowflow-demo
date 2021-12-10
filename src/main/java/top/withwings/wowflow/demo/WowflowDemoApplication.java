package top.withwings.wowflow.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WowflowDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WowflowDemoApplication.class, args);
    }

}
