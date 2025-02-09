package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.auth.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.RegisterUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.ResetPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.VerifyEmailRequestDto;
import com.sasindu.springsecurity.abstractions.enums.AppUserRoles;
import com.sasindu.springsecurity.abstractions.enums.OtpEmailType;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.abstractions.interfaces.IUserOtpService;
import com.sasindu.springsecurity.constants.ApplicationConstants;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.entities.UserOtp;
import com.sasindu.springsecurity.exceptions.BadRequestException;
import com.sasindu.springsecurity.exceptions.ConflictException;
import com.sasindu.springsecurity.exceptions.ForbiddenException;
import com.sasindu.springsecurity.exceptions.UnAuthorizedException;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import com.sasindu.springsecurity.repository.IUserRepository;
import com.sasindu.springsecurity.security.jwt.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


/**
 * The Auth service - handles all authentication related operations
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository _userRepository;
    private final JWTUtils _jwtUtils;
    private final PasswordEncoder _passwordEncoder;
    private final AuthenticationManager _authenticationManager;
    private final EmailNotificationService _emailNotificationService;
    private final IUserOtpService _userOtpService;

    @Value("${application.environment}")
    String environment;

    @Value("${jwt.access.expiration.minutes}")
    String jwtAccessExpireStringMinutes;

    @Value("${jwt.refresh.expiration.days}")
    String refreshTokenExpireStringDays;


    /**
     * Set the cookies - re-usable private method
     *
     * @param access The access token
     * @param refresh The refresh token
     * @param response The response object
     * @param accessMaxAge The access token max age
     * @param refreshMaxAge The refresh token max age
     */
    private void setCookies(String access, String refresh, HttpServletResponse response, int accessMaxAge, int refreshMaxAge) {
        try {
            Cookie accessCookie = new Cookie("access", access);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(environment.equals("production"));
            accessCookie.setPath("/");
            accessCookie.setMaxAge(accessMaxAge);

            Cookie refreshCookie = new Cookie("refresh", refresh);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(environment.equals("production"));
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(refreshMaxAge);

            // Add the cookies to the response
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Register a new user
     *
     * @param request The request object
     * @return The registered user
     */
    @Override
    public AppUser register(RegisterUserRequestDto request) {
        try{
            AppUser user = new AppUser();

            if(_userRepository.existsByEmail(request.getEmail())){
                throw new ConflictException("Email is already taken");
            }

            if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
                throw new BadRequestException("Passwords do not match");
            }

            if(!HelperUtilMethods.isPasswordStrong(request.getPassword())){
                throw new BadRequestException("Password is not strong enough");
            }

            String otp = HelperUtilMethods.generateOtp();

            user.setPassword(_passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(AppUserRoles.ROLE_USER.toString());

            // save the user
            AppUser savedUser =  _userRepository.save(user);

            // set the otp
            UserOtp userOtp = new UserOtp();
            userOtp.setOtpProperties(ApplicationConstants.EMAIL_OTP_EXPIRATION_MINUTES, otp, OtpEmailType.EMAIL, savedUser);
            _userOtpService.saveOtp(userOtp);

            // send the email
            _emailNotificationService.sendEmailVerificationOtpEmail(request.getEmail(), otp);

            // return the saved user
            return savedUser;
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Login a user - set Cookies in the response
     *
     * @param request The request object
     * @throws UnAuthorizedException If the credentials are invalid
     * @throws RuntimeException If an error occurs
     */
    @Override
    public void login(LoginRequestDto request, HttpServletResponse response) {
        try{
            Authentication auth = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Set the authentication object to the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Get authenticated user directly from auth object
            AppUser user = (AppUser) auth.getPrincipal();

            if(!user.isEnabled()){
                throw new ForbiddenException("Please verify your email to login");
            }

            String access = _jwtUtils.generateAccessToken(user);
            String refresh = _jwtUtils.generateRefreshToken(user);

            setCookies(
                    access,
                    refresh,
                    response,
                    Integer.parseInt(jwtAccessExpireStringMinutes) * 60,
                    Integer.parseInt(refreshTokenExpireStringDays) * 60 * 60 * 24
            );
        }
        catch(InternalAuthenticationServiceException | BadCredentialsException e){
            throw new UnAuthorizedException("Invalid credentials");
        }
        catch(DisabledException e){
            throw new ForbiddenException("Please verify your email to login");
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get the authenticated user
     *
     * @return The authenticated user
     */
    @Override
    public AppUser getAuthenticatedUser() {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return (AppUser) auth.getPrincipal();
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Refresh the access token
     *
     * @param request The request object
     * @param response The response object
     */
    @Override
    public void refreshToken(HttpServletRequest request,  HttpServletResponse response) {
        try{
            String refresh = HelperUtilMethods.getCookieFromRequest(request, "refresh");

            if(!_jwtUtils.isRefreshTokenValid(refresh)){
                throw new ForbiddenException("Invalid refresh token");
            }

            Long userId = _jwtUtils.getUserIdFromRefreshToken(refresh);
            AppUser user = _userRepository.findById(userId)
                    .orElseThrow(() -> new ForbiddenException("Invalid refresh token"));
            String access = _jwtUtils.generateAccessToken(user);
            String newRefresh = _jwtUtils.generateRefreshToken(user);

            setCookies(
                    access,
                    newRefresh,
                    response,
                    Integer.parseInt(jwtAccessExpireStringMinutes) * 60,
                    Integer.parseInt(refreshTokenExpireStringDays) * 60 * 60 * 24
            );
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Logout - Clear the cookies in the response
     *
     * @param response The response object
     */
    @Override
    public void logout(HttpServletResponse response) {
        try{
            setCookies(
                    null,
                    null,
                    response,
                    0,
                    0
            );
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Send email verification otp email
     *
     * @param email The email
     */
    @Override
    public void sendEmailVerificationOtpEmail(String email) {
        try{
            AppUser user = Optional.ofNullable(_userRepository.findByEmail(email))
                    .orElseThrow(() -> new BadRequestException("Invalid email"));

            if(user.getIsEmailVerified()){
                throw new BadRequestException("Email is already verified");
            }

            String otp = HelperUtilMethods.generateOtp();

            // set the otp
            UserOtp foundOtp = _userOtpService.findByTypeAndUserId(OtpEmailType.EMAIL, user.getId());
            if(foundOtp != null){
                _userOtpService.deleteByTypeAndUserId(OtpEmailType.EMAIL, user.getId());
            }
            UserOtp userOtp = new UserOtp();
            userOtp.setOtpProperties(ApplicationConstants.EMAIL_OTP_EXPIRATION_MINUTES, otp, OtpEmailType.EMAIL, user);
            _userOtpService.saveOtp(userOtp);

            // send the email and save the user
            _emailNotificationService.sendEmailVerificationOtpEmail(email, otp);
            _userRepository.save(user);
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Send password reset otp email
     *
     * @param email The email
     */
    @Override
    public void sendPasswordResetOtpEmail(String email) {
        try {
            AppUser user = Optional.ofNullable(_userRepository.findByEmail(email))
                    .orElseThrow(() -> new BadRequestException("Invalid email"));

            if(!user.getIsEmailVerified()){
                throw new BadRequestException("Please verify your email first");
            }

            String otp = HelperUtilMethods.generateOtp();

            // set the otp
            UserOtp foundOtp = _userOtpService.findByTypeAndUserId(OtpEmailType.PASSWORD, user.getId());
            if(foundOtp != null){
                _userOtpService.deleteByTypeAndUserId(OtpEmailType.PASSWORD, user.getId());
            }
            UserOtp userOtp = new UserOtp();
            userOtp.setOtpProperties(ApplicationConstants.PASSWORD_OTP_EXPIRATION_MINUTES, otp, OtpEmailType.PASSWORD, user);
            _userOtpService.saveOtp(userOtp);

            // send the email and save the user
            _emailNotificationService.sendPasswordResetOtpEmail(email, otp);
            _userRepository.save(user);
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Verify the email
     *
     * @param request The request object of type VerifyEmailRequestDto
     */
    @Override
    public void verifyEmail(VerifyEmailRequestDto request) {
        try{
            AppUser user = Optional.ofNullable(_userRepository.findByEmail(request.getEmail()))
                    .orElseThrow(() -> new BadRequestException("Invalid email"));

            if(user.getIsEmailVerified()){
                throw new BadRequestException("Email is already verified");
            }

            UserOtp foundOtp = _userOtpService.findByTypeAndUserId(OtpEmailType.EMAIL, user.getId());
            if(foundOtp == null){
                throw new BadRequestException("No OTP found, Please request a new OTP");
            }

            if(foundOtp.isOtpCorrect(request.getOtp())){
                throw new BadRequestException("Invalid OTP");
            }

            if(foundOtp.isOtpExpired()){
                _userOtpService.deleteByTypeAndUserId(OtpEmailType.EMAIL, user.getId());
                throw new BadRequestException("OTP has expired");
            }


            // delete the otp and update the user
            _userOtpService.deleteByTypeAndUserId(OtpEmailType.EMAIL, user.getId());

            // update the user and save
            user.setIsEmailVerified(true);
            _userRepository.save(user);
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Reset the password
     *
     * @param request The request object of type ResetPasswordRequestDto
     */
    @Override
    public void resetPassword(ResetPasswordRequestDto request) {
        try{
            AppUser user = Optional.ofNullable(_userRepository.findByEmail(request.getEmail()))
                    .orElseThrow(() -> new BadRequestException("Invalid email"));

            UserOtp foundOtp = _userOtpService.findByTypeAndUserId(OtpEmailType.PASSWORD, user.getId());
            if(foundOtp == null){
                throw new BadRequestException("No OTP found, Please request a new OTP");
            }

            if(foundOtp.isOtpCorrect(request.getOtp())){
                throw new BadRequestException("Invalid OTP");
            }

            if(foundOtp.isOtpExpired()){
                _userOtpService.deleteByTypeAndUserId(OtpEmailType.PASSWORD, user.getId());
                throw new BadRequestException("OTP has expired");
            }

            if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
                throw new BadRequestException("Passwords do not match");
            }

            if(!HelperUtilMethods.isPasswordStrong(request.getPassword())){
                throw new BadRequestException("Password is not strong enough");
            }

            // delete the otp and update the user
            _userOtpService.deleteByTypeAndUserId(OtpEmailType.PASSWORD, user.getId());

            user.setPassword(_passwordEncoder.encode(request.getPassword()));
            _userRepository.save(user);
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
