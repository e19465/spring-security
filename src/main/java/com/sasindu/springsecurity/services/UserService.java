package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.IUserService;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository _userRepository;


    /**
     * Gets the details of a user account by id
     * @param id - User id
     * @return AppUser
     */
    @Override
    public AppUser getUserAccountDetailsById(Long id) {
        try{
            return _userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppUser updateUserAccount(UpdateUserRequestDto request, Long id) {
        return null;
    }

    @Override
    public void updateUserPassword(UpdateUserPasswordRequestDto request, Long id) {

    }

    @Override
    public void deleteUserAccountById(Long id) {

    }
}
