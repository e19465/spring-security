package com.sasindu.springsecurity.entities;


import com.sasindu.springsecurity.abstractions.enums.OtpEmailType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserOtp {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OtpEmailType type;

    private String otp;

    private LocalDateTime expires;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (name = "user_id", nullable = false)
    private AppUser user;


    /**
     * Checks if the OTP is correct
     * @param otp - OTP to check
     * @return boolean
     */
    public boolean isOtpCorrect(String otp) {
        return this.otp.equals(otp);
    }

    /**
     * Checks if the OTP is expired
     * @return boolean
     */
    public boolean isOtpExpired() {
        return LocalDateTime.now().isAfter(expires);
    }
}
