package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Override
    public User getUserAccountDetailsById(Long id) {
        return null;
    }

    @Override
    public User updateUserAccount(UpdateUserRequestDto request, Long id) {
        return null;
    }

    @Override
    public void updateUserPassword(UpdateUserPasswordRequestDto request, Long id) {

    }

    @Override
    public void deleteUserAccountById(Long id) {

    }
}
