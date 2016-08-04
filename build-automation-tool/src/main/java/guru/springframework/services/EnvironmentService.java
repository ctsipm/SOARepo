package guru.springframework.services;


import guru.springframework.domain.Environment;

public interface EnvironmentService {
    Iterable<Environment> listAllEnvironment();

    Environment getEnvironment(Integer id);

    Environment saveEnvironment(Environment envDetails);

    void deleteEnvironment(Integer id);
}
