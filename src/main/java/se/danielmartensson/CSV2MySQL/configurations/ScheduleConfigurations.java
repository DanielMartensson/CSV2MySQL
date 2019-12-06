package se.danielmartensson.CSV2MySQL.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@PropertySource("classpath:schedule.properties")
@ConditionalOnProperty(name = "schedule.enable", matchIfMissing = true)
public class ScheduleConfigurations {

}
