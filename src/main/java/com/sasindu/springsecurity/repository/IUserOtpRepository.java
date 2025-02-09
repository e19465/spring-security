package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.abstractions.enums.OtpEmailType;
import com.sasindu.springsecurity.entities.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for the UserOtp entity
 */
public interface IUserOtpRepository  extends JpaRepository<UserOtp, Long> {

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
}
