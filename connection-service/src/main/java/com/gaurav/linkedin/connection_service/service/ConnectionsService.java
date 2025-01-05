package com.gaurav.linkedin.connection_service.service;

import com.gaurav.linkedin.connection_service.auth.UserContextHolder;

import com.gaurav.linkedin.connection_service.entity.Person;

import com.gaurav.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.gaurav.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.gaurav.linkedin.connection_service.repository.PersonRepository;
import com.gaurav.linkedin.user_service.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {
    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    public Person createPersonNode(UserCreatedEvent user){
        Person person = new Person();
        person.setUserId(user.getUserId());
        person.setName(user.getName());
        personRepository.save(person);
        return person;
    }

    public List<Person> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("getting first degree connection for user id: {}",userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send Connection request, sender:{}, receiver:{}",senderId,receiverId);
        boolean alreadySendRequest = personRepository.connectionRequestExists(senderId,receiverId);
        boolean alreadyConnected = personRepository.alreadyConnected(senderId,receiverId);

         if (senderId.equals(receiverId)){
             throw new RuntimeException("Both sender and receiver are same");
         }

        if(alreadySendRequest){
            throw new RuntimeException("Connection request already exists, cannot send again");
        }

        if(alreadyConnected){
            throw new RuntimeException("Already Connected users, cannot add connection request");
        }

        log.info("Successfully sent connection request");
        personRepository.addConnectionRequest(senderId,receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        sendRequestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);
        return true;



    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);
        boolean alreadyConnected = personRepository.alreadyConnected(senderId,receiverId);


        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists to accept");

        }

        personRepository.acceptConnectionRequest(senderId,receiverId);

        log.info("Successfully accept connection request, sender: {}, receiver: {}",senderId,receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        acceptRequestKafkaTemplate.send("accept-connection-request-topic",acceptConnectionRequestEvent);

        return true;


    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);

        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists, cannot reject");

        }

        personRepository.rejectConnectionRequest(senderId,receiverId);
        return true;






    }
}
