package com.toilamanh.toilamanh.service.impl;

import com.toilamanh.toilamanh.dto.LoginRequest;
import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.dto.UserDTO;
import com.toilamanh.toilamanh.entity.User;
import com.toilamanh.toilamanh.exception.OurException;
import com.toilamanh.toilamanh.repository.UserRepository;
import com.toilamanh.toilamanh.service.interfac.IUserService;
import com.toilamanh.toilamanh.utils.JWTUtils;
import com.toilamanh.toilamanh.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository userRepository;
    ObjectFactory<PasswordEncoder> passwordEncoderFactory;
    ObjectFactory <JWTUtils> jwtUtilsFactory;
    ObjectFactory<AuthenticationManager> authenticationManagerFactory;
    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " Already Exists");
            }

            user.setPassword(passwordEncoderFactory.getObject().encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntitytoUserDTO(savedUser);

            response.setStatusCode(HttpStatus.CREATED.value());
            response.setUser(userDTO);
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error Occurred During User Registration: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManagerFactory.getObject().authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException(loginRequest.getEmail() + " Not Found"));

            var token = jwtUtilsFactory.getObject().generateToken(user);
            response.setStatusCode(HttpStatus.OK.value());
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successfully logged in");

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error Occurred During User Login: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(users);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successfully retrieved all users");
            response.setUserList(userDTOList);
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getting all users: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successfully retrieved user booking history");
            response.setUser(userDTO);

        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getting all users: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();
        try {
            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            userRepository.deleteById(userId);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successfully deleted user");
        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getting deleteUser: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(Long userId) {
        Response response = new Response();
        try {
            User user =  userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntitytoUserDTO(user);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successfully retrieved user");
            response.setUser(userDTO);
        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getUserById: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();
        try {
            User user =  userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntitytoUserDTO(user);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("successfully retrieved user");
            response.setUser(userDTO);
        }catch (OurException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error getMyInfo: " + e.getMessage());
        }
        return response;
    }
}
