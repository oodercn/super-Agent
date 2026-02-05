package net.ooder.examples.routeagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RouteAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(RouteAgentApplication.class, args);
        System.out.println("Route Agent started successfully");
    }
}
