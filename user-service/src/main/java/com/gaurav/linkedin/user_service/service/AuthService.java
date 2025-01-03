package com.gaurav.linkedin.user_service.service;


import com.gaurav.linkedin.user_service.dto.LoginRequestDto;
import com.gaurav.linkedin.user_service.dto.SignupRequestDto;
import com.gaurav.linkedin.user_service.dto.UserDto;
import com.gaurav.linkedin.user_service.entity.User;
import com.gaurav.linkedin.user_service.event.UserCreatedEvent;
import com.gaurav.linkedin.user_service.exceptions.BadRequestException;
import com.gaurav.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.gaurav.linkedin.user_service.repository.UserRepository;
import com.gaurav.linkedin.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, UserCreatedEvent> kafkaTemplate;

    private final JwtService jwtService;
    public UserDto signUp(SignupRequestDto signupRequestDto) {

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if (exists){
            throw new BadRequestException("User Already exists , cannot signup again");
        }

        User user = modelMapper.map(signupRequestDto,User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        //TODO create Node for user in Neo4j DB
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .build();
        kafkaTemplate.send("user-created-topic",userCreatedEvent);

        return modelMapper.map(savedUser,UserDto.class);



    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));
        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(),user.getPassword());

        if (!isPasswordMatch){
            throw new BadRequestException("Incorrect Password");
        }

        return jwtService.generateAccessToken(user);


    }
}
