package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.dto.request.user.AddUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.LoginRequestDto;
import com.sasindu.springsecurity.entities.AppUser;

import java.util.Map;

public interface IAuthService {
    AppUser register(AddUserRequestDto request);
    Map<String, String> login(LoginRequestDto request);
    AppUser getAuthenticatedUser();
}
