package com.gaurav.linkedin.connection_service.service;

import com.gaurav.linkedin.connection_service.auth.UserContextHolder;

import com.gaurav.linkedin.connection_service.dto.ConnectionStatusDto;
import com.gaurav.linkedin.connection_service.dto.InstituteDto;
import com.gaurav.linkedin.connection_service.entity.Institute;
import com.gaurav.linkedin.connection_service.entity.Person;

import com.gaurav.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.gaurav.linkedin.connection_service.event.EventType;
import com.gaurav.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.gaurav.linkedin.connection_service.repository.InstituteRepository;
import com.gaurav.linkedin.connection_service.repository.PersonRepository;
import com.gaurav.linkedin.user_service.event.ProfileUpdatedEvent;
import com.gaurav.linkedin.user_service.event.UserCreatedEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {
    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;
    private final InstituteRepository instituteRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;



    public Person createPersonNode(UserCreatedEvent user){
        Person person = new Person();
        person.setUserId(user.getUserId());
        person.setName(user.getName());
        personRepository.save(person);
        return person;
    }

    public List<Person> getFirstDegreeConnections(Long userId){
        log.info("getting first degree connection for user id: {}",userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId,HttpServletRequest request) {
        Long senderId = UserContextHolder.getCurrentUserId();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResolutionException("Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
//        log.info("Token: {} ",token);
        String userName = jwtService.getUserNameFromToken(token);


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
                .senderUserName(userName)
                .eventType(EventType.SEND_CONNECTION)
                .build();
        sendRequestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);
        return true;



    }

    public Boolean acceptConnectionRequest(Long senderId,HttpServletRequest request) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResolutionException("Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
//        log.info("Token: {} ",token);
        String userName = jwtService.getUserNameFromToken(token);



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
                .senderUserName(userName)
                .eventType(EventType.ACCEPT_CONNECTION)
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

    public void createPersonInstituteRelation(ProfileUpdatedEvent profileUpdatedEvent) {
        // Retrieve the Person node by userId
        Person person = personRepository.findByUserId(profileUpdatedEvent.getUserId())
                .orElseThrow(() -> new RuntimeException("Person node not found for userId: "
                        + profileUpdatedEvent.getUserId()));

        // Retrieve the Institute node by ID
        Optional<Institute> instituteOptional = instituteRepository.findById(profileUpdatedEvent.getInstituteId());

        if (instituteOptional.isEmpty()) {
            throw new RuntimeException("Institute node not found for id: "
                    + profileUpdatedEvent.getInstituteId());
        }

        // Establish the AFFILIATED_WITH relationship
        Institute institute = instituteOptional.get();
        person.setInstitute(institute);

        // Save the Person node with the relationship
        personRepository.save(person);

        // Increment the number of students connected to the institute
        institute.setNumberOfStudents(institute.getNumberOfStudents() + 1);
        instituteRepository.save(institute);
    }

    public List<InstituteDto> getAllInstitutes(HttpServletRequest request) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("User Id at getAllInstitutes: {}",userId);
        List<Institute> institutes = new ArrayList<>();
        if (userId!=null){
            institutes =  instituteRepository.findAll();
        }
        log.info("User Id at getAllInstitutes: {}",userId);
        return institutes
                .stream()
                .map((element)->modelMapper.map(element,InstituteDto.class))
                .collect(Collectors.toList());
    }

    public ConnectionStatusDto getConnectionStatus(Long userId) {

        Long myUserId = UserContextHolder.getCurrentUserId();

        boolean isRequested = personRepository.connectionRequestExists(userId,myUserId);
        boolean isConnected = personRepository.alreadyConnected(userId,myUserId);

        return ConnectionStatusDto.builder()
                .isConnected(isConnected)
                .isRequested(isRequested)
                .build();

    }

    public List<Long> getConnectedUserId() {
        Long userId = UserContextHolder.getCurrentUserId();
        return personRepository.getConnectedUserId(userId);
    }
}
