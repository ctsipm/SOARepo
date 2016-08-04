package guru.springframework.repositories;

import guru.springframework.domain.Broker;

import org.springframework.data.repository.CrudRepository;

public interface BrokerRepository extends CrudRepository<Broker, Integer>{
}
