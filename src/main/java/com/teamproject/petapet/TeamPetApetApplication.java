package com.teamproject.petapet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@EnableJpaAuditing
@SpringBootApplication
public class TeamPetApetApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamPetApetApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){return new HiddenHttpMethodFilter();}

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
