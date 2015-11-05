package org.sipr.core.config;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.sipr.core.SpringExtension.SpringExtProvider;

@Configuration
public class ActorSystemConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("SipRActorSystem");
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }
}
