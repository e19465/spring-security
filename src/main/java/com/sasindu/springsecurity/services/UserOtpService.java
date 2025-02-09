package com.sasindu.springsecurity.services;

import com.sasindu.springsecurity.abstractions.enums.OtpEmailType;
import com.sasindu.springsecurity.abstractions.interfaces.IUserOtpService;
import com.sasindu.springsecurity.entities.UserOtp;
import com.sasindu.springsecurity.repository.IUserOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserOtpService implements IUserOtpService {
    private final IUserOtpRepository _userOtpRepository;


    /**
     * Find the OTP by type and user ID
     * @param type - Type of the OTP
     * @param userId - ID of the user
     * @return UserOtp
     */
    @Override
    public UserOtp findByTypeAndUserId(OtpEmailType type, Long userId) {
        try{
            return _userOtpRepository.findByTypeAndUserId(type, userId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Delete the OTP by type and user ID
     * @param type - Type of the OTP
     * @param userId - ID of the user
     */
    @Override
    @Transactional
    public void deleteByTypeAndUserId(OtpEmailType type, Long userId) {
        try{
            _userOtpRepository.deleteByTypeAndUserId(type, userId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Save the OTP
     * @param userOtp - UserOtp object
     * @return UserOtp
     */
    @Override
    public UserOtp saveOtp(UserOtp userOtp) {
        try{
            return _userOtpRepository.save(userOtp);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
