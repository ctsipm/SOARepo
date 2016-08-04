package guru.springframework.services;

import guru.springframework.domain.Broker;
import guru.springframework.repositories.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerServiceImpl implements BrokerService {
    private BrokerRepository brokerRepository;

    @Autowired
    public void setBrokerRepository(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    @Override
    public Iterable<Broker> listAllBrokers() {
        return brokerRepository.findAll();
    }

    @Override
    public Broker getBrokerById(Integer id) {
        return brokerRepository.findOne(id);
    }

    @Override
    public Broker saveBroker(Broker broker) {
        return brokerRepository.save(broker);
    }

    @Override
    public void deleteBroker(Integer id) {
        brokerRepository.delete(id);
    }
}
