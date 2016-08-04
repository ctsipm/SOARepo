package guru.springframework.services;


import guru.springframework.domain.Queuemanager;

public interface QueuemanagerService {
    Iterable<Queuemanager> listAllQueuemanagers();

    Queuemanager getQueuemanagerById(Integer id);

    Queuemanager saveQueuemanager(Queuemanager product);

    void deleteQueuemanager(Integer id);
}
