package at.qe.event_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Spring boot application. Execute maven with <code>mvn spring-boot:run</code>
 * to start this web application.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@SpringBootApplication
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
/*
 * Prevent spring from trying to autowire the websocket-infrastructure: Exclude
 * the at.qe.skeleton.ui.websockets package from component scan.
 *
 * NOTE: Do not add any components to this package which should be managed by
 * spring. It is reserved for the CDI-injection-mechanisms (Weld). Only add
 * CDI-managed components.
 */
@ComponentScan(basePackages = "at.qe.event_manager", excludeFilters = @Filter(type = FilterType.REGEX, pattern = "at.qe.event_manager.ui.websockets.*"))
public class Main {

    public static void main(String[] args) {
            SpringApplication.run(Main.class, args);
    }
}
