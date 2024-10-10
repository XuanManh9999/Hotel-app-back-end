package com.toilamanh.toilamanh.service.interfac;

import com.toilamanh.toilamanh.dto.LoginRequest;
import com.toilamanh.toilamanh.dto.Response;
import com.toilamanh.toilamanh.entity.User;

public interface IUserService {
    Response register(User user);
    Response login (LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory (Long userId);
    Response deleteUser (Long userId);
    Response getUserById (Long userId);
    Response getMyInfo (String email);
}
