package guru.springframework.bootstrap;

import guru.springframework.domain.Environment;
import guru.springframework.repositories.EnvironmentRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EnvironmentLoader implements ApplicationListener<ContextRefreshedEvent> {

    private EnvironmentRepository envDetailRepository;

    private Logger log = Logger.getLogger(EnvironmentLoader.class);

    @Autowired
    public void setEnvironmentRepository(EnvironmentRepository envDetailRepository) {
        this.envDetailRepository = envDetailRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Environment shirt = new Environment();
        shirt.setDescription("SANDBOX for testing");
        shirt.setName("SANDBOX");
        
        //envDetailRepository.save(shirt);

        log.info("Saved Shirt - id: " + shirt.getId());

    }
}
