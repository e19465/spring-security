package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.enums.OtpEmailType;
import com.sasindu.springsecurity.entities.UserOtp;

public interface IUserOtpService {

    /**
     * Find the OTP by type and user ID
     * @param type - Type of the OTP
     * @param userId - ID of the user
     * @return UserOtp
     */
    UserOtp findByTypeAndUserId(OtpEmailType type, Long userId);


    /**
     * Delete the OTP by type and user ID
     * @param type - Type of the OTP
     * @param userId - ID of the user
     */
    void deleteByTypeAndUserId(OtpEmailType type, Long userId);


    /**
     * Save the OTP
     * @param userOtp - UserOtp object
     * @return UserOtp
     */
    UserOtp saveOtp(UserOtp userOtp);
}
